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

import com.google.gson.annotations.Expose;
import com.ltu.fm.auth.AWSAuth;
import com.ltu.fm.model.user.User;


/**
 * Bean for the user login request.
 * @author uyphu
 */
public class LoginUserResponse {
	
	/** The token. */
	@Expose
    private String token;
    
    /** The expires. */
	@Expose
    private long expires;
    
    /** The type. */
	@Expose
    private String type;
    
	/** The item. */
	@Expose
    private User item;
	
	@Expose
	private AWSAuth auth;

	/**
	 * Gets the token.
	 *
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * Sets the token.
	 *
	 * @param token the new token
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * Gets the expires.
	 *
	 * @return the expires
	 */
	public long getExpires() {
		return expires;
	}

	/**
	 * Sets the expires.
	 *
	 * @param expires the new expires
	 */
	public void setExpires(long expires) {
		this.expires = expires;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * Gets the item.
	 *
	 * @return the item
	 */
	public User getItem() {
		return item;
	}

	/**
	 * Sets the item.
	 *
	 * @param item the new item
	 */
	public void setItem(User item) {
		this.item = item;
	}
	
	public AWSAuth getAuth() {
		return auth;
	}

	public void setAuth(AWSAuth auth) {
		this.auth = auth;
	}

	/**
	 * Instantiates a new login user response.
	 */
	public LoginUserResponse() {
		
	}

	/**
	 * Instantiates a new login user response.
	 *
	 * @param token the token
	 * @param expires the expires
	 * @param type the type
	 */
	public LoginUserResponse(String token, long expires, String type) {
		super();
		this.token = token;
		this.expires = expires;
		this.type = type;
	}
    
}
