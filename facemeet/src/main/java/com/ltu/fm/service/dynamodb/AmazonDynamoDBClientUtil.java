package com.ltu.fm.service.dynamodb;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

/**
 * The Class AmazonDynamoDBClientUtil.
 * @author uyphu
 */
public class AmazonDynamoDBClientUtil {

	/** The dynamo db. */
	private static AmazonDynamoDBClient dynamoDB = null;

	/**
	 * Gets the single instance of AmazonDynamoDBClientUtil.
	 *
	 * @return single instance of AmazonDynamoDBClientUtil
	 */
	public static AmazonDynamoDBClient getInstance() {
		if (dynamoDB == null) {
			dynamoDB = new AmazonDynamoDBClient();
			dynamoDB.setRegion(Region.getRegion(Regions.US_EAST_1));
		}
		return dynamoDB;
	}
	
}
