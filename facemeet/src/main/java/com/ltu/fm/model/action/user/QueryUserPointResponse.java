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

import java.util.List;

import com.ltu.fm.model.geo.UserPoint;

/**
 * Bean for the pet creation response.
 */
public class QueryUserPointResponse {
	
    private List<UserPoint> items;
    
    private String nextPageToken;

	public List<UserPoint> getItems() {
		return items;
	}

	public void setItems(List<UserPoint> items) {
		this.items = items;
		if (items != null && !items.isEmpty()) {
			this.nextPageToken = items.get(items.size()-1).getUserId();
		}
	}

	public String getNextPageToken() {
		return nextPageToken;
	}

	public void setNextPageToken(String nextPageToken) {
		this.nextPageToken = nextPageToken;
	}

}
