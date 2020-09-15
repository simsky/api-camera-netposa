package com.welton.video.np;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.welton.micro.common.RestResponseException;
import com.welton.video.np.jna.NpNetSdk;

import feign.Feign;
import feign.Logger.Level;
import feign.slf4j.Slf4jLogger;

@Service
public class NpApiService {
	private static final Logger logger = LoggerFactory.getLogger(NpApiService.class);
	public static HashMap<String, Integer> userTicketMap = new HashMap<String, Integer>();

	public String login(String host, Integer port, String username, String password) {
		// 启动环境中设置参数，正式发布包中去掉这部分代码
		if (!System.getProperties().contains("jna.encoding")) {
			System.setProperty("jna.encoding", "GBK");
		}
		
		byte[] infoBuf = new byte[1024];
		int[] realLen = new int[1];
		int result = NpNetSdk.INSTANCE.NP_NET_GetTicket(host, port, username, password, infoBuf, infoBuf.length,
				realLen);
		String ticket = new String(infoBuf, 0, realLen[0]);
		logger.info("[NP_NET_GetTicket]: {}, {}", result, ticket);
		if (result != 0) {
			throw RestResponseException.mkMsg(result, "Fail to call NP_NET_GetTicket.");
		}

		int[] userHandle = new int[1];
		//// 备注：ticket传入的buff长度必须大于ticket长度，否则获取失败【ticket用于前端播放视频】
		result = NpNetSdk.INSTANCE.NP_NET_LoginByTicket(userHandle, host, port, infoBuf, realLen[0]);
		logger.info("[NP_NET_LoginByTicket]: {}, {}", result, userHandle[0]);

		if (result != 0) {
			throw RestResponseException.mkMsg(result, "Fail to call NP_NET_LoginByTicket.");
		}

		result = NpNetSdk.INSTANCE.NP_NET_Init();
		logger.debug("[NP_NET_Init]: {}", result);

		// 保存ticket，后续调用传入ticket
		userTicketMap.put(ticket, userHandle[0]);
		return ticket;
	}

	public int logout(String ticket) {
		int userHD = getUserHandle(ticket);
		int result = NpNetSdk.INSTANCE.NP_NET_Logout(userHD);
		userTicketMap.remove(ticket);

		return result;
	}

	private int getUserHandle(String ticket) {
		if (!userTicketMap.containsKey(ticket)) {
			throw RestResponseException.mkMsg(-95, "Invalid ticket");
		}

		int userHandle = userTicketMap.get(ticket);
		boolean[] connect = new boolean[1];
		NpNetSdk.INSTANCE.NP_NET_IsConnected(userHandle, connect);
		if (!connect[0]) {
			userTicketMap.remove(ticket);
			throw RestResponseException.mkMsg(-95, "Ticket is expired, please re-login.");
		}

		return userHandle;
	}

	public JSONArray listObjects(String ticket, int type, String serName) {
		JSONArray result = new JSONArray();
		int userHD = getUserHandle(ticket);
		NpNetSdk.NpSdkCallback callback = new NpNetSdk.NpSdkCallback() {
			@Override
			public boolean handle(String userParam, int userHandle, int objType, String objName, String objInfo) {
				logger.debug("NP_NET_ListObjects Callback: {}, {}, {}", objType, objName, objInfo);

				JSONObject obj = JSON.parseObject(objInfo);
				result.add(obj);
				return true;
			}
		};

		int[] count = new int[1];
		// NPNetObjectType.NPNET_TYPE_CAMERA
		int retCode = NpNetSdk.INSTANCE.NP_NET_ListObjects(userHD, type, serName, count, callback, "param");

		if (retCode != 0) {
			throw RestResponseException.mkMsg(retCode, "Please check ErrorHelp.h .");
		}

		return result;
	}

	public JSONArray listOffline(String ticket) {
		int bufLen = 10240;
		int[] realLen = new int[1];
		byte[] infoBuf = new byte[bufLen];

		// 设备数量过多时，会存在buffer不够使用的情况，需要调整
		int retCode = NpNetSdk.INSTANCE.NP_NET_GetCameraStatus(getUserHandle(ticket), null, infoBuf, bufLen, realLen);
		logger.debug("NP_NET_GetCameraStatus: {}, {}", retCode, JnaCharsetUtil.cvtString(infoBuf, 0, realLen[0]));
		if (retCode != 0) {
			throw RestResponseException.mkMsg(retCode, "Please check ErrorHelp.h .");
		}

		String str = new String(infoBuf, 0, realLen[0]);
		JSONArray obj = JSON.parseArray(str);

		return obj;
	}

	private static HashMap<Integer, EventPushRemote> map = new HashMap<Integer, EventPushRemote>();

	/**
	 * 
	 * @param ticket
	 * @param url
	 */
	public int eventSubscribe(String ticket, String server, String path) {
		int userHD = getUserHandle(ticket);

		// 推送 HTTP POST JSON
		EventPushRemote eventPush = Feign.builder().logger(new Slf4jLogger()).logLevel(Level.FULL)
				.target(EventPushRemote.class, server);
		map.put(userHD, eventPush);

		NpNetSdk.OnEventCallback eventCallback = new NpNetSdk.OnEventCallback() {
			@Override
			public void handle(String userParam, int userHD, String happenTime, String objName, String objInfo,
					int eventType, int status) {
				logger.debug("NP_NET_SubscribeEvent Callback:{} {}, {}, {}, {}, {}, {}", userParam, userHD, happenTime,
						objName, objInfo, eventType, status);

				EventPushRemote remote = map.get(userHD);
				if (remote != null) {
					remote.push(objInfo, path);
				}
			}
		};

		// Test
		eventPush.push("{}", path);

		int result = NpNetSdk.INSTANCE.NP_NET_SubscribeEvent(userHD, eventCallback, path);
		logger.debug("NP_NET_SubscribeEvent: {}", result);
		return result;
	}

	public int eventCancelSubscribe(String ticket) {
		int userHD = getUserHandle(ticket);
		int result = NpNetSdk.INSTANCE.NP_NET_CancelEventSubscription(userHD);
		logger.debug("NP_NET_CancelEventSubscription: {}", result);

		return result;
	}

	public int ptzControl(String ticket, String avPath, int cmd, int param) {
		int userHD = getUserHandle(ticket);
		int result = NpNetSdk.INSTANCE.NP_NET_PtzControl(userHD, avPath, cmd, param);
		logger.debug("NP_NET_PtzControl: {}", result);

		return result;
	}

	public int ptzControl3d(String ticket, String avPath, int direct, float x, float y, float w, float h) {
		int userHD = getUserHandle(ticket);
		int result = NpNetSdk.INSTANCE.NP_NET_PtzControl3D(userHD, avPath, direct, x, y, w, userHD);
		logger.debug("NP_NET_PtzControl3D: {}", result);

		return result;
	}

	public int ptzLock(String ticket, String avPath, int lockTime) {
		int userHD = getUserHandle(ticket);
		int result = NpNetSdk.INSTANCE.NP_NET_PtzLock(userHD, avPath, lockTime);
		logger.debug("NP_NET_PtzLock: {}", result);

		return result;
	}

	public int ptzAuxControl(String ticket, String avPath, int num, boolean control) {
		int userHD = getUserHandle(ticket);
		int result = NpNetSdk.INSTANCE.NP_NET_PtzAuxControl(userHD, avPath, num, control);
		logger.debug("NP_NET_PtzAuxControl: {}", result);

		return result;
	}

	public JSONArray queryRecord(String ticket, String avPath, int vodType, String beginTime, String endTime) {
		JSONArray result = new JSONArray();

		int userHD = getUserHandle(ticket);
		NpNetSdk.ListSegmentCallback fnOnListSegment = new NpNetSdk.ListSegmentCallback() {
			@Override
			public boolean handle(String userParam, int userHD, String avPath, String beginTime, String endTime) {
				logger.debug("NP_NET_QueryRecord Callback: {}, {}, {}", avPath, beginTime, endTime);
				HashMap<String, String> record = new HashMap<String, String>();
				record.put("avPath", avPath);
				record.put("beginTime", beginTime);
				record.put("endTime", endTime);
				result.add(record);
				return true;
			}
		};

		int retCode = NpNetSdk.INSTANCE.NP_NET_QueryRecord(userHD, avPath, vodType, beginTime, endTime, fnOnListSegment,
				"");
		if (retCode != 0) {
			throw RestResponseException.mkMsg(retCode, "fail to invoke NP_NET_QueryRecord");
		}

		return result;
	}
}
