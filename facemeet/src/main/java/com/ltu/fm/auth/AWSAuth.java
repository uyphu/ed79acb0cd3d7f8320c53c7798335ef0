package com.ltu.fm.auth;

import com.google.gson.annotations.Expose;
import com.ltu.fm.model.user.UserCredentials;

public class AWSAuth {

	@Expose
	private String identityId;
	@Expose
	private String token;
	@Expose
	private UserCredentials credentials;

	public String getIdentityId() {
		return identityId;
	}

	public void setIdentityId(String identityId) {
		this.identityId = identityId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public UserCredentials getCredentials() {
		return credentials;
	}

	public void setCredentials(UserCredentials credentials) {
		this.credentials = credentials;
	}

	public AWSAuth() {
		
	}
	
	public AWSAuth(String identityId, String token, UserCredentials credentials) {
		this.identityId = identityId;
		this.token = token;
		this.credentials = credentials;
	}
}
