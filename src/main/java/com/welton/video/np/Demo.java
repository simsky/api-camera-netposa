package com.welton.video.np;

import java.nio.charset.Charset;

import com.welton.video.np.jna.NPNetObjectType;
import com.welton.video.np.jna.NpNetSdk;
import com.welton.video.np.jna.NpPlayerSdk;

public class Demo {

	public static void main(String[] args) {
		System.out.println("Default Charset=" + Charset.defaultCharset());
		System.out.println("file.encoding=" + System.getProperty("file.encoding"));
		if (!System.getProperties().contains("jna.encoding")) {
			System.setProperty("jna.encoding", "GBK");
		}

		int result, BUF_SIZE = 1024;
		int[] userHandle = new int[1];

		// ByteBuffer infoBuf = ByteBuffer.allocateDirect(1024);
		byte[] infoBuf = new byte[BUF_SIZE];
		int[] realLen = new int[1];

		result = NpNetSdk.INSTANCE.NP_NET_GetSDK_Version(infoBuf, 1024, realLen);
		System.out.format("[NP_NET_GetSDK_Version]: %d, %d, %s\r\n", result, realLen[0],
				new String(infoBuf, 0, realLen[0]));

		// 有回调方法的必须先初始化此方法 listObject
		result = NpNetSdk.INSTANCE.NP_NET_Init();
		System.out.format("[NP_NET_Init]: %d\r\n", result);

		result = NpNetSdk.INSTANCE.NP_NET_GetTicket("192.168.1.80", 2015, "test", "123456", infoBuf, infoBuf.length,
				realLen);
		// 备注：ticket传入的buff长度必须大于ticket长度，否则获取失败【ticket用于前端播放视频】
		byte[] ticket = new byte[realLen[0] + 1];
		System.arraycopy(infoBuf, 0, ticket, 0, realLen[0]);
		System.out.format("[NP_NET_GetTicket]: %d, %s\r\n", result, new String(ticket));

		result = NpNetSdk.INSTANCE.NP_NET_LoginByTicket(userHandle, "192.168.1.80", 2015, ticket, ticket.length - 1);
		System.out.format("[NP_NET_LoginByTicket]: %d, %s\r\n", result, userHandle[0]);

//		result = NetSdk.INSTANCE.NP_NET_Login(userHandle, "192.168.1.80", 2015, "test", "123456");
//		System.out.format("[NP_NET_Login]: %d, %s\r\n", result, userHandle[0]);

//		result = NpNetSdk.INSTANCE.NP_NET_GetServerVersion(userHandle[0], infoBuf, 1024, realLen);
//		System.out.format("[NP_NET_GetServerVersion]: %d, %d, %s\r\n", result, realLen[0],
//				new String(infoBuf, 0, realLen[0]));
//
//		infoBuf = alloc(BUF_SIZE);
//		result = NpNetSdk.INSTANCE.NP_NET_GetUserData(userHandle[0], infoBuf, infoBuf.length, realLen);
//		System.out.format("NP_NET_GetUserData: %d, %s\r\n", result, new String(infoBuf, 0, realLen[0]));

//		String objInfo="{\"name\"=\"192.168.1.64\", \"title\"=\"demo title\", \"url\"=\"hikhost:192.168.1.64:8000/password=Welton123&username=admin\"}";
//		result = NetSdk.INSTANCE.NP_NET_AddObject(userHandle[0], NPNetObjectType.NPNET_TYPE_DEVICE, "e5474491-8ee9-4bda-a0a0-84ffba954517",objInfo);
//		System.out.format("NP_NET_AddObject: %d \r\n", result);

//		result = NetSdk.INSTANCE.NP_NET_DelObject(userHandle[0], NPNetObjectType.NPNET_TYPE_DEVICE, "0Q0v0Q0v3Wk8C81");
//		System.out.format("NP_NET_DelObject: %d \r\n", result);

		manage(userHandle);

		// play(userHandle);

	}

	/**
	 * 管理接口（组织，设备添加）
	 */
	public static void manage(int[] userHandle) {
		int userHD = userHandle[0];
		int result = -1, BUF_SIZE = 1024;
		byte[] infoBuf = alloc(BUF_SIZE);
		int[] realLen = new int[1];

		NpNetSdk.NpSdkCallback callback = new NpNetSdk.NpSdkCallback() {
			public boolean handle(String userParam, int userHandle, int objType, String objName, String objInfo) {
				System.out.format("NP_NET_ListObjects Callback: %s, %s, %s\r\n", objType, objName, objInfo);
				return true;
			}
		};
		result = NpNetSdk.INSTANCE.NP_NET_ListObjects(userHandle[0], NPNetObjectType.NPNET_TYPE_CAMERA, "", realLen,
				callback, "test");

		infoBuf = alloc(BUF_SIZE * 2);
		int[] orgCount = new int[1], pvgCount = new int[1], bufLen = { BUF_SIZE * 2 }, dataLen = new int[1];
		result = NpNetSdk.INSTANCE.NP_NET_GetOrganization(userHandle[0], infoBuf, bufLen, dataLen, orgCount, pvgCount);
		System.out.format("NP_NET_GetOrganization: %d, %s, %d, %d, %d\r\n", result,
				JnaCharsetUtil.cvtString(infoBuf, 0, dataLen[0]), dataLen[0], orgCount[0], pvgCount[0]);

//		// 设备状态
//		String objName = "0Q0v0Q0v3Wk8C81";
//		int[] statePoint = new int[1];
//		result = NpNetSdk.INSTANCE.NP_NET_GetObjectStatus(userHandle, NPNetObjectType.NPNET_TYPE_CAMERA, objName, statePoint);
//		System.out.format("NP_NET_GetObjectStatus: %d, %d\r\n", result, statePoint[0]);
//		
		result = NpNetSdk.INSTANCE.NP_NET_GetCameraStatus(userHD, null, infoBuf, BUF_SIZE, realLen);
		System.out.format("NP_NET_GetCameraStatus: %d, %s\r\n", result,
				JnaCharsetUtil.cvtString(infoBuf, 0, realLen[0]));

		// 添加节点

		// 删除节点

		// 时间订阅和取消
		// subscribe(userHD);
		String avPath = "0Q0v0Q0v3Wk8C81";
		String beginTime = "2020-02-26 00:00:00";
		String endTime = "2020-02-26 23:59:59";
		NpNetSdk.ListSegmentCallback fnOnListSegment = new NpNetSdk.ListSegmentCallback() {

			@Override
			public boolean handle(String userParam, int userHD, String avPath, String beginTime, String endTime) {
				System.out.format("NP_NET_QueryRecord Callback: %s, %s, %s\r\n", avPath, beginTime, endTime);
				return true;
			}
		};
		result = NpNetSdk.INSTANCE.NP_NET_QueryRecord(userHD, avPath, 1, beginTime, endTime, fnOnListSegment, "param");
		System.out.format("NP_NET_QueryRecord: %d \r\n", result);
	}

	private static void subscribe(int userHD) {
		// 事件
		NpNetSdk.OnEventCallback eventCallback = new NpNetSdk.OnEventCallback() {
			@Override
			public void handle(String userParam, int userHD, String happenTime, String objName, String objInfo,
					int eventType, int status) {
				System.out.format("NP_NET_SubscribeEvent Callback:%s %d, %s, %s, %s, %d, %d\r\n", userParam, userHD,
						happenTime, objName, objInfo, eventType, status);
			}
		};

		int result = NpNetSdk.INSTANCE.NP_NET_SubscribeEvent(userHD, eventCallback, "");
		System.out.format("NP_NET_SubscribeEvent: %d\r\n", result);

		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		result = NpNetSdk.INSTANCE.NP_NET_CancelEventSubscription(userHD);
		System.out.format("NP_NET_CancelEventSubscription: %d\r\n", result);
	}

	/**
	 * 媒体播放(播放器窗口需要研究)
	 * 
	 * @param userHandle
	 */
	public static void play(int[] userHandle) {
		int result, BUF_SIZE = 1024;

		// Create Player
		int active = -1;
		// active = User32.INSTANCE.GetDesktopWindow();
		// active = User32.INSTANCE.CreateWindowEx(0, " ", " ", 0, 100, 100, 900, 700,
		// null, null, null, null);
		int[] playHD = new int[1];
		NpPlayerSdk.VidioDecodeCallback videoDcodeCallback = new NpPlayerSdk.VidioDecodeCallback() {
			@Override
			public void handle(int playHD, String buf, int len, NpPlayerSdk.NPPlayFrameInfo fInfo, String userParam) {
				System.out.format("NpSdkCallback: %s, %s, %s\r\n", playHD, fInfo, userParam);
			}
		};
		result = NpPlayerSdk.INSTANCE.NP_PLAY_CreatePlayer(playHD, active, 0, videoDcodeCallback, "");
		System.out.format("NP_PLAY_CreatePlayer: %d,  %d, %s\r\n", result, playHD[0], active);

		// Open Real Stream
		int[] streamHD = new int[1];
		String avPath = "0Q0v0Q0v3Wk8C81";// 不是使用的path，使用的为设备的name
		NpNetSdk.OnStreamDataCallback onStreamDataCallback = new NpNetSdk.OnStreamDataCallback() {
			public boolean handle(String userParam, int streamHD, final byte[] data, int dataLen) {
				System.out.format("OnStreamDataCallback: %s, %s, %s\r\n", userParam, streamHD,
						new String(data, 0, dataLen));
				return true;
			}
		};
		result = NpNetSdk.INSTANCE.NP_NET_StartStream(userHandle[0], streamHD, avPath, null, null, "");
		System.out.format("NP_NET_StartStream: %d,  %d\r\n", result, streamHD[0]);

		// I帧请求
		result = NpNetSdk.INSTANCE.NP_NET_StreamRequestIFrame(streamHD[0]);
		System.out.format("NP_NET_StreamRequestIFrame: %d \r\n", result);

		System.out.println("wait end ... (10s)");
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Stop Stream
		result = NpNetSdk.INSTANCE.NP_NET_StopStream(streamHD[0]);
		System.out.format("NP_NET_StopStream: %d \r\n", result);

		// Release Player
		result = NpPlayerSdk.INSTANCE.NP_PLAY_ReleasePlayer(playHD[0]);
		System.out.format("NP_PLAY_ReleasePlayer: %d \r\n", result);
	}

	public static byte[] alloc(int len) {
		return new byte[len];
	}
}
