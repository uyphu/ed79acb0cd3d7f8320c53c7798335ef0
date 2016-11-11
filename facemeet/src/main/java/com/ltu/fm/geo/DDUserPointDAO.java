package com.ltu.fm.geo;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
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
import com.ltu.fm.dao.AbstractDao;
import com.ltu.fm.exception.DAOException;
import com.ltu.fm.model.user.User;
import com.ltu.fm.model.user.UserDAO;


public class DDUserPointDAO extends AbstractDao<UserPoint> implements UserPointDAO {
	
	private static DDUserPointDAO instance = null;
	
	private static GeoDataManager geoDataManager = null;
	
	private static GeoDataManagerConfiguration config = null;
	
	public static DDUserPointDAO getInstance() {
		if (instance == null) {
			config = new GeoDataManagerConfiguration(client, DynamoDBConfiguration.USER_POINT_TABLE_NAME);
			geoDataManager = new GeoDataManager(config);
			instance = new DDUserPointDAO();
		}

		return instance;
	}

	protected DDUserPointDAO() {
		super(UserPoint.class);
	}
	
	private void putPoint(User user) throws IOException, JSONException {
		GeoPoint geoPoint = new GeoPoint(user.getLat(),user.getLng());
		AttributeValue rangeKeyAttributeValue = new AttributeValue().withS(user.getId());
		AttributeValue schoolNameKeyAttributeValue = new AttributeValue().withS(user.getDisplayName());

		PutPointRequest putPointRequest = new PutPointRequest(geoPoint, rangeKeyAttributeValue);
		putPointRequest.getPutItemRequest().addItemEntry(DynamoDBConfiguration.USER_POINT_TABLE_NAME, schoolNameKeyAttributeValue);

		PutPointResult putPointResult = geoDataManager.putPoint(putPointRequest);
		

	}

	@Override
	public User putUser(User user) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

}