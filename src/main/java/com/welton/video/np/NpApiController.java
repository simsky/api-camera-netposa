package com.welton.video.np;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.welton.micro.common.rsp.ResultData;
import com.welton.video.np.jna.NPNetObjectType;

@RestController
@RequestMapping(value = "/api/np")
public class NpApiController {

	@Autowired
	private NpApiService npApi;

	/**
	 * 视频管理账号登录
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/login", method = { RequestMethod.GET })
	public ResultData login(@RequestParam Map<String, String> params) {

		String host = params.get("host");
		String portStr = params.get("port");
		String username = params.get("username");
		String password = params.get("password");

		String ticket = npApi.login(host, Integer.parseInt(portStr), username, password);
		return ResultData.ok(ticket);
	}
	
	/**
	 * 视频管理账号登录
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/logout", method = { RequestMethod.GET })
	public ResultData logout(@RequestParam String ticket) {
		int result = npApi.logout(ticket);
		return ResultData.result(result);
	}
	
	/**
	 * 视频管理账号登录
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/ticket", method = { RequestMethod.GET })
	public ResultData ticket(@RequestParam Map<String, String> params) {

		String host = params.get("host");
		String portStr = params.get("port");
		String username = params.get("username");
		String password = params.get("password");

		String ticket = npApi.login(host, Integer.parseInt(portStr), username, password);
		return ResultData.ok(ticket);
	}

	@RequestMapping(value = "/list/camera", method = { RequestMethod.GET })
	public ResultData listCamera(@RequestParam String ticket) {

		JSONArray result = npApi.listObjects(ticket, NPNetObjectType.NPNET_TYPE_CAMERA, "");
		return ResultData.ok(result);
	}

	@RequestMapping(value = "/list/objects", method = { RequestMethod.GET })
	public ResultData listCamera(@RequestParam String ticket, @RequestParam int type) {

		JSONArray result = npApi.listObjects(ticket, type, "");
		return ResultData.ok(result);
	}

	@RequestMapping(value = "/list/offline", method = { RequestMethod.GET })
	public ResultData listOffline(@RequestParam String ticket) {

		JSONArray result = npApi.listOffline(ticket);
		return ResultData.ok(result);
	}

	/**
	 * 订阅推送路径，必须是POST方式接受消息，内容为JSON格式
	 * 
	 * @param ticket
	 * @param server 服务器，格式 http://host:port
	 * @param path   路径： /xx/xxx
	 * @return
	 */
	@RequestMapping(value = "/event/subscribe", method = { RequestMethod.GET })
	public ResultData eventSubscribe(@RequestParam String ticket, @RequestParam String server,
			@RequestParam String path) {

		int result = npApi.eventSubscribe(ticket, server, path);
		return ResultData.result(result);
	}

	@RequestMapping(value = "/event/cancel", method = { RequestMethod.GET })
	public ResultData eventCancelSubscribe(@RequestParam String ticket) {

		int result = npApi.eventCancelSubscribe(ticket);
		return ResultData.result(result);
	}

	@RequestMapping(value = "/event/receive", method = { RequestMethod.POST })
	public void eventReceive(@RequestBody String json) {
		System.out.format("Test Push: %s\r\n", json);
	}

	@RequestMapping(value = "/ptz/control", method = { RequestMethod.GET })
	public ResultData ptzControl(@RequestParam String ticket, @RequestParam String avPath, @RequestParam int cmd,
			@RequestParam int param) {

		int result = npApi.ptzControl(ticket, avPath, cmd, param);
		return ResultData.result(result);
	}

	@RequestMapping(value = "/ptz/control3d", method = { RequestMethod.GET })
	public ResultData ptzControl3d(@RequestParam String ticket, @RequestParam String avPath, @RequestParam int direct,
			@RequestParam float x, @RequestParam float y, @RequestParam float w, @RequestParam float h) {

		int result = npApi.ptzControl3d(ticket, avPath, direct, x, y, w, h);
		return ResultData.result(result);
	}

	@RequestMapping(value = "/ptz/lock", method = { RequestMethod.GET })
	public ResultData ptzLock(@RequestParam String ticket, @RequestParam String avPath, @RequestParam int lockTime) {

		int result = npApi.ptzLock(ticket, avPath, lockTime);
		return ResultData.result(result);
	}

	@RequestMapping(value = "/ptz/aux", method = { RequestMethod.GET })
	public ResultData ptzAuxControl(@RequestParam String ticket, @RequestParam String avPath, @RequestParam int num,
			@RequestParam boolean control) {

		int result = npApi.ptzAuxControl(ticket, avPath, num, control);
		return ResultData.result(result);
	}

	@RequestMapping(value = "/record/list", method = { RequestMethod.GET })
	public ResultData recordList(@RequestParam String ticket, @RequestParam String avPath, @RequestParam int vodType,
			@RequestParam String beginTime, @RequestParam String endTime) {

		JSONArray result = npApi.queryRecord(ticket, avPath, vodType, beginTime, endTime);
		return ResultData.ok(result);
	}
}
