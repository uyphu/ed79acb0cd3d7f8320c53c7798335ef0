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
package com.ltu.fm.model.action.device;

import com.ltu.fm.model.device.Device;

/**
 * Bean for the pet creation response.
 */
public class DeleteDeviceResponse {
    private Device item;

	public Device getItem() {
		return item;
	}

	public void setItem(Device item) {
		this.item = item;
	}
}
