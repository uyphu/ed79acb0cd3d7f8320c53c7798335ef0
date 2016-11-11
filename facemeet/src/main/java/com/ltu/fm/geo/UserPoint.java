package com.ltu.fm.geo;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import com.amazonaws.geo.GeoDataManager;
import com.amazonaws.geo.GeoDataManagerConfiguration;
import com.amazonaws.geo.model.GeoPoint;
import com.amazonaws.geo.model.PutPointRequest;
import com.amazonaws.geo.model.PutPointResult;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import com.ltu.fm.configuration.DynamoDBConfiguration;
import com.ltu.fm.model.user.User;


public class UserPoint {
	
	private static UserPoint instance = null;
	
	private static GeoDataManager geoDataManager = null;
	
	private static GeoDataManagerConfiguration config = null;
	
	public static UserPoint getInstance() {
		if (instance == null) {
			instance = new UserPoint();
		}

		return instance;
	}

	protected UserPoint() {
		config = new GeoDataManagerConfiguration(ddb, tableName);
		geoDataManager = new GeoDataManager(config);
	}
	
	private void putPoint(User user) throws IOException, JSONException {
//		GeoPoint geoPoint = new GeoPoint(requestObject.getDouble("lat"), requestObject.getDouble("lng"));
		GeoPoint geoPoint = new GeoPoint(user.getLat(),user.getLng());
		//AttributeValue rangeKeyAttributeValue = new AttributeValue().withS(UUID.randomUUID().toString());
		AttributeValue rangeKeyAttributeValue = new AttributeValue().withS(user.getId());
		//AttributeValue schoolNameKeyAttributeValue = new AttributeValue().withS(requestObject.getString("schoolName"));
		AttributeValue schoolNameKeyAttributeValue = new AttributeValue().withS(user.getDisplayName());

		PutPointRequest putPointRequest = new PutPointRequest(geoPoint, rangeKeyAttributeValue);
		putPointRequest.getPutItemRequest().addItemEntry(DynamoDBConfiguration.USER_POINT_TABLE_NAME, schoolNameKeyAttributeValue);

		PutPointResult putPointResult = geoDataManager.putPoint(putPointRequest);

		printPutPointResult(putPointResult, out);
	}

}
