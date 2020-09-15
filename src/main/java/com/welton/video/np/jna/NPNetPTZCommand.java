package com.welton.video.np.jna;

/**
 * NPNET_PTZ_PANRIGHT 
云台右转 
NPNET_PTZ_RIGHTUP 
云台上仰和右转 
NPNET_PTZ_TILTUP 
云台上仰 
NPNET_PTZ_LEFTUP 
云台上仰和左转 
NPNET_PTZ_PANLEFT 
云台左转 
NPNET_PTZ_LEFTDOWN 
云台下俯和左转 
NPNET_PTZ_TILTDOWN 
云台下俯 
NPNET_PTZ_RIGHTDOWN 
云台下俯和右转 
NPNET_PTZ_SCAN 
云台左右自动扫描 
NPNET_PTZ_HALT 
云台停止 
NPNET_PTZ_IRIS 
光圈变化(param>0扩大、param<0缩小、param=0表示停止) 
NPNET_PTZ_ZOOM_IN 
焦距变大(param>0倍率变大、param<0倍率变小、param=0表示停止) 
NPNET_PTZ_FOCUS 
聚焦调整(param>0前调、param<0后调、param=0表示停止) 
NPNET_PTZ_VIEW 
转到指定预置位 
NPNET_PTZ_SETVIEW 
设置当前位置为一个预置位 
NPNET_PTZ_AUX 
接通辅助设备开关(param=1 表示开,param=0 表示关,其它值无效) 
NPNET_PTZ_WASH 
清洗 
NPNET_PTZ_WIPE 
接通雨刷开关  
NPNET_PTZ_LIGHT 
接通灯光电源 
NPNET_PTZ_POWER 
电源 
Remarks
    云台最大速度，#define NPNET_PTZ_MAX_SPEED 15
 *
 */
public class NPNetPTZCommand {
	public final static int NPNET_PTZ_PANRIGHT = 0;
	public final static int NPNET_PTZ_RIGHTUP = 1;
	public final static int NPNET_PTZ_TILTUP = 2;
	public final static int NPNET_PTZ_LEFTUP = 3;
	public final static int NPNET_PTZ_PANLEFT = 4;
	public final static int NPNET_PTZ_LEFTDOWN = 5;
	public final static int NPNET_PTZ_TILTDOWN = 6;
	public final static int NPNET_PTZ_RIGHTDOWN = 7;
	public final static int NPNET_PTZ_SCAN = 8;
	public final static int NPNET_PTZ_HALT = 9;
	public final static int NPNET_PTZ_IRIS = 10;
	public final static int NPNET_PTZ_ZOOM_IN = 11;
	public final static int NPNET_PTZ_FOCUS = 12;
	public final static int NPNET_PTZ_VIEW = 13;
	public final static int NPNET_PTZ_SETVIEW = 14;
	public final static int NPNET_PTZ_AUX = 15;
	public final static int NPNET_PTZ_WASH = 16;
	public final static int NPNET_PTZ_WIPE = 17;
	public final static int NPNET_PTZ_LIGHT = 18;
	public final static int NPNET_PTZ_POWER = 19;
}
