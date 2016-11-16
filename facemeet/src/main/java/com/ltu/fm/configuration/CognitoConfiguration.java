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
package com.ltu.fm.configuration;


/**
 * Configuration parameters for the Cognito credentials provider.
 * @author uyphu
 */
public class CognitoConfiguration {
	// TODO: Specify the identity pool id
	/** The Constant IDENTITY_POOL_ID. */
	public static final String IDENTITY_POOL_ID = "us-east-1:5a52146d-167d-447d-a1d4-9ae556c5f5d4";
	//public static final String IDENTITY_POOL_ID = S3ResourceLoaderUtil.getProperty(AppConfiguration.IDENTITY_POOL_ID);
    
	// TODO: Specify the custom provider name used by the identity pool
    /** The Constant CUSTOM_PROVIDER_NAME. */
	public static final String CUSTOM_PROVIDER_NAME = "com.kins.vc";
    //public static final String CUSTOM_PROVIDER_NAME = S3ResourceLoaderUtil.getProperty(AppConfiguration.CUSTOM_PROVIDER_NAME);

    /** The Constant COGNITO_PROVIDER_NAME. */
    // This should not be changed, it is a default value for Cognito.
    public static final String COGNITO_PROVIDER_NAME = "cognito-identity.amazonaws.com";
    
    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
		System.out.println(IDENTITY_POOL_ID);
		System.out.println(CUSTOM_PROVIDER_NAME);
	}
    
}
