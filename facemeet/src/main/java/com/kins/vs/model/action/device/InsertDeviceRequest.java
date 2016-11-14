package com.kins.vs.model.action.device;

/**
 * The Class InsertDeviceRequest.
 */
public class InsertDeviceRequest {

	private String phoneId;
	private String type;
	private String pushToken;
	private String lastLoginUserId;

	public String getPhoneId() {
		return phoneId;
	}

	public void setPhoneId(String phoneId) {
		this.phoneId = phoneId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPushToken() {
		return pushToken;
	}

	public void setPushToken(String pushToken) {
		this.pushToken = pushToken;
	}

	public String getLastLoginUserId() {
		return lastLoginUserId;
	}

	public void setLastLoginUserId(String lastLoginUserId) {
		this.lastLoginUserId = lastLoginUserId;
	}
	
	public InsertDeviceRequest() {
		
	}

	public InsertDeviceRequest(String phoneId, String type, String pushToken, String lastLoginUserId) {
		this.phoneId = phoneId;
		this.type = type;
		this.pushToken = pushToken;
		this.lastLoginUserId = lastLoginUserId;
	}
	
}
