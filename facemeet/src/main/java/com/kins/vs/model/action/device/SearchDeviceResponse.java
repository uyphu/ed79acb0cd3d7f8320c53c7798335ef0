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
package com.kins.vs.model.action.device;

import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.kins.vs.model.device.Device;

/**
 * Bean for the pet creation response.
 */
public class SearchDeviceResponse {
	
    private List<Device> items;
    
    private String nextPageToken;

	public List<Device> getItems() {
		return items;
	}

	public void setItems(List<Device> items) {
		this.items = items;
		if (items != null && !items.isEmpty()) {
			this.nextPageToken = items.get(items.size()-1).getId();
		}
	}
	
	public void setItems(QueryResultPage<Device> list) {
		if (list != null) {
			items = list.getResults();
			if (list.getLastEvaluatedKey() != null) {
				nextPageToken = list.getLastEvaluatedKey().get("id").getS();
			}
		}
	}

	public String getNextPageToken() {
		return nextPageToken;
	}

	public void setNextPageToken(String nextPageToken) {
		this.nextPageToken = nextPageToken;
	}

}
