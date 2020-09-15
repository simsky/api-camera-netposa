package com.welton.video.np.jna;

public class NPNetObjectType {
	/**
	 * 
	 */
	public final static int NPNET_TYPE_UNKNOWN = 0;
	public final static int NPNET_TYPE_GATEWAY = 0x0002;
	//设备类型
	public final static int NPNET_TYPE_DEVICE = 0x0004;
	/**
	 * NPNET_TYPE_CAMERA
	 * 通道类型，属性值如下： 
	 * name : 通道名称 
	 * title : 通道标题 
	 * path : 通道所在路径（通常用来组织目录结构） 
	 * level : 通道等级（与用户等级相关联） 
	 * avType : 通道类型，0－不支持云台；1－支持云台；其它－未使用 
	 * host : 通道所属设备名称 
	 * addr : 通道对应设备上的通道号
	 */
	public final static int NPNET_TYPE_CAMERA = 0x0010;
	public final static int NPNET_TYPE_MONITOR = 0x0020;
	public final static int NPNET_TYPE_AUDIO = 0x0040;
	public final static int NPNET_TYPE_TRUNKIN = 0x0100;
	public final static int NPNET_TYPE_TRUNKOUT = 0x0200;
	public final static int NPNET_TYPE_ALARMIN = 0x1000;
	public final static int NPNET_TYPE_ALARMOUT = 0x2000;
}
