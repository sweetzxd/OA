package com.oa.core.helper;

public class JsonResult {

	/**
	 * 是否成功，0代表成功，非0代表失败，失败时error信息不为空
	 */
	private String status;
	/**
	 * 错误信息
	 */
	private String error;
	/**
	 * 内容
	 */
	private String message;

	public static JsonResult fail(String status, String error) {
		JsonResult result = new JsonResult();
		result.setStatus(status);
		result.setError(error);
		return result;
	}

	public static JsonResult fail(String error) {
		return fail("1", error);
	}

	public static JsonResult success(String message) {
		JsonResult result = new JsonResult();
		result.setStatus("0");
		result.setMessage(message);
		return result;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
