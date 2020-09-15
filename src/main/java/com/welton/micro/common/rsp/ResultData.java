package com.welton.micro.common.rsp;

import java.io.Serializable;

import com.welton.micro.common.RestResponseException;

/**
 * 此类作为模块化通用返回对象。兼容CommonResponse
 * 
 * @author yekaihe
 *
 */
public class ResultData implements Serializable {

	private static final long serialVersionUID = -1104991211790932246L;

	private int code;
	private String message;
	private String desc;
	private Object data;
	private long timestamp;

	public ResultData() {
		this.timestamp = System.currentTimeMillis();
	}

	public static ResultData exception(RestResponseException exp) {
		return error(exp.getErrorCode(), exp.getMessage(), exp.getDesc(), null);
	}

	public static ResultData result(int code) {
		if (code == 0) {
			return ok();
		} else {
			return error(code, "error");
		}
	}
	
	public static ResultData error(int code) {
		// 需要根据错误码，查招message和desc资源
		return error(code, "system error");
	}

	public static ResultData error(String msg) {
		// 需要根据错误码，查招message和desc资源
		return error(-1, msg);
	}

	public static ResultData error(int code, String msg) {
		return error(code, msg, null, null);
	}

	public static ResultData error(int code, String msg, String desc, Object data) {
		ResultData result = new ResultData();
		result.code = code;
		result.message = msg;
		result.desc = desc;
		result.data = data;
		return result;
	}

	public static ResultData ok(Object data) {
		ResultData result = new ResultData();
		result.setData(data);
		result.setMessage("ok");
		return result;
	}

	public static ResultData ok() {
		return ok(null);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
}