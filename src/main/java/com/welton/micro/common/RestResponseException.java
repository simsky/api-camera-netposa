package com.welton.micro.common;

/**
 * 该类用于承接通用的reponse异常后转成标准输出结果。
 * 如果该类不继承TowerException，需要在CommonExceptionHandler中添加处理
 * 1.返回统一格式的的异常数据
 * 2.统一记录调试（业务级）日志
 * 
 * @author yekaihe
 *
 */
public class RestResponseException extends RuntimeException {
	private static final long serialVersionUID = 2928694034701782774L;

	private int errorCode = 0;
	private String desc;
	
	
	public RestResponseException(Throwable t) {
		super(t);
		errorCode = -1;
	}
	
	/**
	 * 错误信息
	 * @param message
	 */
	public RestResponseException(String message){
		super(message);
		errorCode = -1;
	}
	


	/**
	 * 错误码+描述
	 * @param errCode
	 * @param message
	 */
	public RestResponseException(int errCode, String message){
		super(message);
		this.errorCode = errCode;
	}
	
	/**
	 * 错误码+描述
	 * @param errCode
	 * @param message
	 */
	public RestResponseException(int errCode, String message, String desc){
		super(message);
		this.errorCode = errCode;
		this.desc = desc;
	}
	
	/**
	 * 统一记录日志（提供给统一异常编码位置用来记录异常日志）
	 * 
	 * message:内部使用
	 * desc:可直接显示到终端	
	 * 
	 * @return
	 */
	public String getDebugMsg() {
		StringBuilder builder = new StringBuilder();
		if (errorCode != -1) {
			builder.append("[").append(errorCode).append("]: ");
		}
		builder.append(getMessage());
		if (desc != null) {
			builder.append(", desc: ").append(desc);
		}

		return builder.toString();
	}
	
//	/**
//	 * 支持日志参数记录方式
//	 * @param message
//	 * @param objects
//	 */
//	public RestResponseException(String message, Object ...objects ){
//		//super(-1, message);
//	}
//	
//	/**
//	 * 支持日志参数记录方式(错误码)
//	 * @param errCode
//	 * @param message
//	 * @param objects
//	 */
//	public RestResponseException(int errCode, String message, Object ...objects ){
//		//super(-1, message);
//	}
	
	/**
	 * 简单异常提示返回
	 * @return
	 */
	public static RestResponseException mkDesc(String desc) {
		RestResponseException e = new RestResponseException(-1, "General exception");
		e.setDesc(desc);
		return e;
	}

	public static RestResponseException mkMsg(int code, String msg) {
		RestResponseException e = new RestResponseException(code, msg);
		return e;
	}
	
	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
