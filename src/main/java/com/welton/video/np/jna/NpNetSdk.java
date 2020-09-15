package com.welton.video.np.jna;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

public interface NpNetSdk extends Library {
	// np_netsdk np_fileplayer np_filesdk np_netsdk_pvg671
	NpNetSdk INSTANCE = (NpNetSdk) Native.load((Platform.isWindows() ? "np_netsdk" : "c"), NpNetSdk.class);

	/** 获得SDK版本信息 */
	int NP_NET_GetSDK_Version(byte[] infoBuf, int len, int[] readLen);

	/** 初始化SDK */
	int NP_NET_Init();

	/** 清除SDK资源 */
	int NP_NET_Cleanup();

	/** 获取认证码 */
	int NP_NET_GetTicket(String ipOrHost, int port, String userName, String passwd, byte[] infoBuf, int len, int[] realLen);
	
	/** 通过认证信息登录服务器。 */
	int NP_NET_LoginByTicket(int[] userHD, String ipOrHost, int port, byte[] ticket, int len);
	
	/** 登录 */
	int NP_NET_Login(Object userHD, String ipOrHost, int port, String userName, String passwd);

	int NP_NET_Logout(int userHD);
	
	/** 获取服务器版本 */
	int NP_NET_GetServerVersion(int userHD, byte[] infoBuf, int len, int[] realLen);

	/** 获取用户数据 */
	int NP_NET_GetUserData(int userHD, byte[] buf, int bufLen, int[] dataLen /* = NULL */
	);

	/** 列出所有设备 */
	int NP_NET_ListObjects(int userHD, int objType, String serName, int[] count, NpSdkCallback fnOnListObj,
			Object userParam);

	public interface NpSdkCallback extends Callback {
		public boolean handle(String userParam, int userHandle, int objType, String objName, String objInfo);
	}

	/** 获取组织结构信息 */
	int NP_NET_GetOrganization(int userHD, byte[] buf, int[] bufLen, int[] dataLen, int[] orgCount, int[] pvgCount);

	int NP_NET_StartStream(int userHD, int[] streamHD, String avPath, OnStreamDataCallback fnData, Callback fnRobbed,
			String userParam);

	public interface OnStreamDataCallback extends Callback {
		public boolean handle(String userParam, int streamHD, final byte[] data, int dataLen);
	}
	
	/**  动态添加对象 */
	int NP_NET_AddObject(int userHD, int objType, String parent, String objInfo);
	
	/** 删除对象 */
	int NP_NET_DelObject(int userHD, int objType, String objName);
	
	/** 获得与服务器的连接状态 */
	int NP_NET_IsConnected( int userHD,  boolean[] connect );
	
	/** 获取对象状态 （此API出错 userHD无法正常通过JNA传入） */
	int NP_NET_GetObjectStatus(int[] userHD, int objType, String ojbName, int[] status);
	
	/** 获取云台状态 备注：实际为获取离线设备，objName无意义*/
	int NP_NET_GetCameraStatus(int userHD, String objName, byte[] buf, int bufLen, int[] realLen);
	
	/** 停止实时视频流 */
	int NP_NET_StopStream(int streamHD);
	
	/** 实时视频请求I帧 */
	int NP_NET_StreamRequestIFrame(int streamHD);
	
	/**  NPNetPTZCommand */
	int NP_NET_PtzControl(int userHD, String avPath, int cmd, int param);
	
	/** 3D云台控制（通过用户句柄）  */
	int NP_NET_PtzControl3D(int userHD, String avPath, int direct, float x, float y, float w, float h);
	
	/** PTZ控制锁定 */
	int NP_NET_PtzLock(int userHD, String avPath, int lockTime);
	
	/** PTZ控制锁定 */
	int NP_NET_PtzAuxControl(int userHD, String avPath, int num, boolean control);
	
	public interface ListSegmentCallback extends Callback {
		/**
		 * 
		 * @param userParam
		 * @param userHD
		 * @param avPath
		 * @param beginTime 时间段开始时间,格式："2012-01-01 13:20:00.000" 
		 * @param endTime
		 * @return TURE -  断续下一次回调  ALSE - 结束回调
		 */
		public boolean handle(String userParam, int userHD, String avPath, String beginTime, String endTime);
	}

	/**
	 * 
	 * @param userHD [in] 登录句柄,由NP_NET_Login()获得
	 * @param avPath [in] 摄像机通道名称 
	 * @param vodType [in] 0-服务器录像, 非0 录像所在的层数,最大值为256 
	 * @param beginTime 起始时间, 格式："2012-01-01 13:20:00.000" 或 "20120101132000000" 
	 * @param endTime [in] 结束时间（不含）
	 * @param fnOnListSegment [in] 回调函数
	 * @param userParam [in] 用户数据
	 * @return 
	 */
	int NP_NET_QueryRecord(int userHD, String avPath, int vodType, String beginTime, String endTime,
			ListSegmentCallback fnOnListSegment, String userParam/* =NULL */
	);
	
	/** 订阅事件 */
	int NP_NET_SubscribeEvent(int userHD, OnEventCallback fnOnEvent, String userParam);

	public interface OnEventCallback extends Callback {
		/**
		 * 
		 * @param userParam 用户参数
		 * @param userHD 用户句柄
		 * @param happenTime 报警发生时间，"2012-01-01 13:20:00.000"(含毫秒)
		 * @param objName 报警对象名称,可能为"$"、"host/"、"av/"、"ai/"等 
		 * @param objInfo 服务器上报的所有信息
		 * @param eventType 事件类型 NpNetEventType
		 * @param status 事件状态,1为报警态,0为正常态
		 * @return 
		 */
		public void handle(String userParam, int userHD, String happenTime, String objName, String objInfo,
				int eventType, int status);
	}

	/** 取消订阅 */
	int NP_NET_CancelEventSubscription(int userHD);
	
}
