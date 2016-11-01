package com.kins.vs.configuration;

/**
 * The Class AppConfiguration.
 * @author Phu Le
 */
public class AppConfiguration {
	

	/** The Constant CONFIG_FILE_NAME. */
	public static final String CONFIG_FILE_NAME = "vc.config.properties";

	/** The Constant BUCKET_NAME. */
	//DEV
	//public static final String BUCKET_NAME = "config-no-deleting";
	//PROD
	public static final String BUCKET_NAME = "vc-config-no-deleting";
	
	/** The web url key. */
	public static String WEB_URL_KEY = "webUrl";
	
	/** The access key. */
	public static String ACCESS_KEY = "accessKey";
	
	/** The secret access key. */
	public static String SECRET_ACCESS_KEY = "secretAccess";
	
	/** The region key. */
	public static String REGION_KEY = "region";
	
	/** The Constant SMTP_USERNAME. */
	public static final String SMTP_USERNAME = "smtpUsername";
	
	/** The Constant SMTP_PASSWORD. */
	public static final String SMTP_PASSWORD = "smtpPassword";
	
	/** The Constant HOST_MAIL. */
	public static final String HOST_MAIL = "hostMail";
	
	/** The Constant FROM_MAIL. */
	public static final String FROM_MAIL = "senderEmail";
	
	/** The Constant ACTIVATION_MESSAGE. */
	public static final String ACTIVATION_MESSAGE = "activationMessage";
	
	/** The Constant IDENTITY_POOL_ID. */
	public static final String IDENTITY_POOL_ID = "identityPoolId";
		
	/** The Constant CUSTOM_PROVIDER_NAME. */
	public static final String CUSTOM_PROVIDER_NAME = "customProviderName";
	
}
