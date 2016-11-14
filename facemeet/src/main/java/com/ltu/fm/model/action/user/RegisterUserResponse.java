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

import com.ltu.fm.model.user.User;


/**
 * Bean for the pet creation response.
 */
public class RegisterUserResponse {
//	private String id;
//
//	public String getId() {
//		return id;
//	}
//
//	public void setId(String id) {
//		this.id = id;
//	}
//
//	@Override
//	public String toString() {
//		return "UserResponse [id=" + id + "]";
//	}
	
	private User item;

	public User getItem() {
		return item;
	}

	public void setItem(User item) {
		this.item = item;
	}

	@Override
	public String toString() {
		return "RegisterUserResponse [item=" + item.toString() + "]";
	}
	
}
