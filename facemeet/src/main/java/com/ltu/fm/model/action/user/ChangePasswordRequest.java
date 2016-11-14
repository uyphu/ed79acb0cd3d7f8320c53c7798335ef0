package com.ltu.fm.model.action.user;


/**
 * The Class InsertDeviceRequest.
 */
public class ChangePasswordRequest {

	private String email;
	private String password;
	private String changeKey;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getChangeKey() {
		return changeKey;
	}
	public void setChangeKey(String changeKey) {
		this.changeKey = changeKey;
	}
	
}