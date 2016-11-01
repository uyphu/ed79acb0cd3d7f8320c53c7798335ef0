package com.kins.vs.configuration;

import com.amazonaws.auth.BasicAWSCredentials;
import com.kins.vs.utils.S3ResourceLoaderUtil;

/**
 * The Class CredentialConfiguration.
 * @author uyphu
 */
public class CredentialConfiguration {

	/**
	 * Gets the aws credentials.
	 *
	 * @return the aws credentials
	 */
	public static BasicAWSCredentials getAwsCredentials() {
		String accessKey = S3ResourceLoaderUtil.getProperty("accessKey");
		String secretAccess = S3ResourceLoaderUtil.getProperty("secretAccess");
		BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretAccess);
		return credentials;
	}
	
}
