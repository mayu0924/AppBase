package com.appheader.base.common.network.entity;

import org.json.JSONObject;

/**
 * 响应实体类
 * @author mayu
 */
public class ResponseEntity {

	private boolean success;

	private String errorCode;

	private String errorMessage;

	private JSONObject dataObject;
	
	private String originalText;

	public JSONObject getDataObject() {
		return dataObject;
	}

	public void setDataObject(JSONObject resultObject) {
		this.dataObject = resultObject;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getOriginalText() {
		return originalText;
	}

	public void setOriginalText(String originalText) {
		this.originalText = originalText;
	}

}
