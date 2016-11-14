/*
 * Copyright 2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package com.ltu.fm.model.action.user;

/**
 * The Class InsertDeviceRequest.
 */
public class MergeUserRequest {

	private String id;
	private String phoneId;
	private String type;
	private String pushToken;
	private String lastLoginUserId;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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
	
	public MergeUserRequest() {
		
	}

	public MergeUserRequest(String phoneId, String type, String pushToken, String lastLoginUserId) {
		this.phoneId = phoneId;
		this.type = type;
		this.pushToken = pushToken;
		this.lastLoginUserId = lastLoginUserId;
	}
	
}
