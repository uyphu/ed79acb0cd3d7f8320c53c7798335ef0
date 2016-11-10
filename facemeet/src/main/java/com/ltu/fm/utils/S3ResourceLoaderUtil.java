package com.ltu.fm.utils;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;
import com.ltu.fm.configuration.AppConfiguration;

/**
 * The Class S3ResourceLoaderUtil.
 * @author uyphu
 */
public class S3ResourceLoaderUtil {
	
	/** The log. */
	private static Logger log = Logger.getLogger(S3ResourceLoaderUtil.class);
	
	/** The props. */
	public static Properties props = new Properties();

	/**
	 * Load properties.
	 */
	private static void loadProperties() {
		 
		InputStream inputStream;
		try {
			String propFileName = AppConfiguration.CONFIG_FILE_NAME;
 
			inputStream = AppUtil.class.getClassLoader().getResourceAsStream(propFileName);
 
			if (inputStream != null) {
				props.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
 
			inputStream.close();
		} catch (Exception e) {
			log.error(e.getMessage(), e.getCause());
		} 
	}
	
	/**
	 * Load s3 properties.
	 */
	private static void loadS3Properties() {
		try {
			AmazonS3 client = new AmazonS3Client();
			
			S3Object xFile = client.getObject(AppConfiguration.BUCKET_NAME, AppConfiguration.CONFIG_FILE_NAME);
			InputStream contents = xFile.getObjectContent();
			if (contents != null) {
				props.load(contents);
			} else {
				throw new FileNotFoundException("property file 'vc.config.properties'  not found in bucket 'config-no-deleting' S3 ");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e.getCause());
		}
	}
	
	/**
	 * Gets the property.
	 *
	 * @param key the key
	 * @return the property
	 */
	public static String getProperty(String key) {
//		if (!props.containsKey(key)) {
//			loadS3Properties();
//		}
		String value = props.getProperty(key);
		if (value == null) {
			if (!props.containsKey(key)) {
				props = new Properties();
				loadProperties();
			}
			value = props.getProperty(key);
		}
		return value;
	}
	
	public static void main(String[] args) {
		System.out.println(getProperty("webUrl"));
	}
}
