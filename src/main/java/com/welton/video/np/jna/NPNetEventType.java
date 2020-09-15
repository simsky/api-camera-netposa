package com.welton.video.np.jna;

/**
 * NPNET_EVENT_UNKNOWN 
未知事件 
NPNET_EVENT_VIDEO_LOST 
视频丢失发生/恢复 
NPNET_EVENT_MOTION 
运动感知发生/恢复 
NPNET_EVENT_VIDEO_HIDE 
视频遮挡发生/恢复 
NPNET_EVENT_VIDEO_BROKEN 
视频断线发生/恢复 
NPNET_EVENT_ALARM_IN 
报警输入报警/恢复 
NPNET_EVENT_CAMERA_LOCK 
摄像机锁定/恢复 
NPNET_EVENT_DIGITAL_STREAM 
数字码流传输发起/停止 
NPNET_EVENT_MANUAL_RECORD 
手动录像发起/停止 
NPNET_EVENT_DEVICE_BROKEN 
设备发生断线/恢复 
NPNET_EVENT_ROFS_ERROR 
ROFS报告的错误发生/恢复 
NPNET_EVENT_SYSTEM_INIT 
系统初始化 
NPNET_EVENT_USER_LOGIN 
用户登入 
NPNET_EVENT_USER_LOGOUT 
用户注销 
NPNET_EVENT_MONITOR_SWITCH 
监视器切换 
NPNET_EVENT_PTZ 
云台控制 
NPNET_EVENT_OPEN_VIDEO_FILE 
打开视频文件 
NPNET_EVENT_CLOSE_VIDEO_FILE 
关闭视频文件 
NPNET_EVENT_ALARM_INFO 
报警设备报告信息 
NPNET_EVENT_VA_ALARM 
智能报警事件 
NPNET_EVENT_OSD_CHANGE 
OSD信息改变 
NPNET_EVENT_CPU_TOO_BUSY 
CPU使用率过高报警 
NPNET_EVENT_MEMORY_TOO_HIGH 
内存使用率过高报警 
NPNET_EVENT_DISK_ARRAY_WRITE_FAILED 
磁盘阵列不能写 
NPNET_EVENT_DISK_ARRAY_ERROR 
磁盘阵列错误 
Remarks
事件类型值小于50事件的有报警态和正常态两种状态,大于50的事件只有报警态
操作类事件通常不会上报
 *
 */
public class NPNetEventType {
	public final static int NPNET_EVENT_UNKNOWN 		= -1;
	public final static int NPNET_EVENT_VIDEO_LOST 		= 0;
	public final static int NPNET_EVENT_MOTION 		= 1;
	public final static int NPNET_EVENT_VIDEO_HIDE  	= 2;
	public final static int NPNET_EVENT_VIDEO_BROKEN 	= 3;
	public final static int NPNET_EVENT_ALARM_IN 		= 10;
	public final static int NPNET_EVENT_CAMERA_LOCK 	= 12;
	public final static int NPNET_EVENT_MONITOR_LOCK 	= 13;
	public final static int NPNET_EVENT_DIGITAL_STREAM 	= 14;
	public final static int NPNET_EVENT_MANUAL_RECORD 	= 15;
	public final static int NPNET_EVENT_DEVICE_BROKEN 	= 16;
	public final static int NPNET_EVENT_ROFS_ERROR 		= 17;
	public final static int NPNET_EVENT_SYSTEM_INIT 	= 50;
	public final static int NPNET_EVENT_USER_LOGIN 		= 51;
	public final static int NPNET_EVENT_USER_LOGOUT 	= 52;
	public final static int NPNET_EVENT_MONITOR_SWITCH 	= 53;
	public final static int NPNET_EVENT_PTZ 		= 54;
	public final static int NPNET_EVENT_OPEN_VIDEO_FILE 	= 55;
	public final static int NPNET_EVENT_CLOSE_VIDEO_FILE 	= 56;
	public final static int NPNET_EVENT_ALARM_INFO 		= 57;
	public final static int NPNET_EVENT_VA_ALARM 		= 58;
	public final static int NPNET_EVENT_OSD_CHANGE 		= 59;
	public final static int NPNET_EVENT_CPU_TOO_BUSY 	= 100;
	public final static int NPNET_EVENT_MEMORY_TOO_HIGH 	= 101;
	public final static int NPNET_EVENT_DISK_ARRAY_WRITE_FAILED = 102;
	public final static int NPNET_EVENT_DISK_ARRAY_ERROR 	= 103;
}
