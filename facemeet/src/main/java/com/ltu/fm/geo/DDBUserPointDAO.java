package com.ltu.fm.geo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;

import com.amazonaws.geo.GeoDataManager;
import com.amazonaws.geo.GeoDataManagerConfiguration;
import com.amazonaws.geo.model.GeoPoint;
import com.amazonaws.geo.model.GetPointRequest;
import com.amazonaws.geo.model.GetPointResult;
import com.amazonaws.geo.model.PutPointRequest;
import com.amazonaws.geo.model.PutPointResult;
import com.amazonaws.geo.model.QueryRadiusRequest;
import com.amazonaws.geo.model.QueryRadiusResult;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.kins.vc.constants.Constants;
import com.kins.vs.model.userevent.UserEvent;
import com.kins.vs.utils.AppUtil;
import com.ltu.fm.configuration.DynamoDBConfiguration;
import com.ltu.fm.dao.AbstractDao;
import com.ltu.fm.exception.DAOException;
import com.ltu.fm.model.user.User;


public class DDBUserPointDAO extends AbstractDao<UserPoint> implements UserPointDAO {
	
	private static DDBUserPointDAO instance = null;
	
	private static GeoDataManager geoDataManager = null;
	
	private static GeoDataManagerConfiguration config = null;
	
	private static ObjectMapper mapper;
	private static JsonFactory factory;
	
	public static DDBUserPointDAO getInstance() {
		if (instance == null) {
			config = new GeoDataManagerConfiguration(client, DynamoDBConfiguration.USER_POINT_TABLE_NAME);
			geoDataManager = new GeoDataManager(config);
			
			mapper = new ObjectMapper();
			factory = mapper.getJsonFactory();
			
			instance = new DDBUserPointDAO();
		}

		return instance;
	}

	protected DDBUserPointDAO() {
		super(UserPoint.class);
	}
	
	@Override
	public PutPointResult putUser(User user) throws DAOException {
		try {
			GeoPoint geoPoint = new GeoPoint(user.getLat(),user.getLng());
			AttributeValue rangeKeyAttributeValue = new AttributeValue().withS(user.getId());
			AttributeValue schoolNameKeyAttributeValue = new AttributeValue().withS(user.getDisplayName());

			PutPointRequest putPointRequest = new PutPointRequest(geoPoint, rangeKeyAttributeValue);
			putPointRequest.getPutItemRequest().addItemEntry("displayName", schoolNameKeyAttributeValue);

			PutPointResult putPointResult = geoDataManager.putPoint(putPointRequest);
			return putPointResult;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOException("Error puting point", e);
		}
	}
	
	public GetPointResult getPoint(String userId, double lat, double lng) {
		GeoPoint geoPoint = new GeoPoint(lat, lng);
		AttributeValue rangeKeyAttributeValue = new AttributeValue().withS(userId);

		GetPointRequest getPointRequest = new GetPointRequest(geoPoint, rangeKeyAttributeValue);
		return geoDataManager.getPoint(getPointRequest);

	}
	
	public List<UserPoint> queryRadius(GeoPoint centerPoint, double radiusInMeter, Integer limit, String cursor) throws DAOException {
		try {
			List<UserPoint> results = new ArrayList<UserPoint>();
			
			List<String> attributesToGet = new ArrayList<String>();
			attributesToGet.add(config.getRangeKeyAttributeName());
			attributesToGet.add(config.getGeoJsonAttributeName());
			attributesToGet.add("displayName");

			QueryRadiusRequest queryRadiusRequest = new QueryRadiusRequest(centerPoint, radiusInMeter);
			queryRadiusRequest.getQueryRequest().setAttributesToGet(attributesToGet);
			queryRadiusRequest.getQueryRequest().setExclusiveStartKey(null);
//			if (cursor != null) {
//				queryRadiusRequest.getQueryRequest().setExclusiveStartKe
//			} else {
//				queryRadiusRequest.getQueryRequest().setExclusiveStartKey(null);
//			}
			if (limit != null) {
				queryRadiusRequest.getQueryRequest().withLimit(4);
			}
			QueryRadiusResult queryRadiusResult = geoDataManager.queryRadius(queryRadiusRequest);
			for (Map<String, AttributeValue> item : queryRadiusResult.getItem()) {
				results.add(toUserPoint(item));
			}
			
			return results;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOException("Error queryin DAO", e);
		}
		
	}
	
	private Map<String, AttributeValue> buildExclusiveStartKeyWithUserId(String cursor) {
		if (cursor == null || cursor.trim().equals(Constants.EMPTY_STRING)) {
			return null;
		}
		
		UserEvent item = find(cursor);
		if (item == null) {
			return null;
		}
		
		Map<String, AttributeValue> exclusiveStartKey = new HashMap<String, AttributeValue>();
		exclusiveStartKey.put("id", new AttributeValue(item.getId()));
		exclusiveStartKey.put("userId", new AttributeValue(item.getUserId()));
		exclusiveStartKey.put("startDate", new AttributeValue(AppUtil.toString(item.getStartDate())));
		return exclusiveStartKey;
	}
	
	private static UserPoint toUserPoint(Map<String, AttributeValue> item) throws DAOException {
		try {
			
			String geoJsonString = item.get(config.getGeoJsonAttributeName()).getS();
			JsonParser jsonParser = factory.createJsonParser(geoJsonString);
			JsonNode jsonNode = mapper.readTree(jsonParser);

			double latitude = jsonNode.get("coordinates").get(0).getDoubleValue();
			double longitude = jsonNode.get("coordinates").get(1).getDoubleValue();
			String rangeKey = item.get(config.getRangeKeyAttributeName()).getS();
			String displayName = "";
			if (item.containsKey("displayName")) {
				displayName = item.get("displayName").getS();
			}
			return new UserPoint(rangeKey, displayName, latitude, longitude);
		} catch (Exception e) {
			throw new DAOException("Error binding data.", e);
		}
		
	}

}
