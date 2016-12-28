package com.ltu.fm.model.geo;

import java.util.ArrayList;
import java.util.Date;
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
import com.amazonaws.geo.model.UpdatePointRequest;
import com.amazonaws.geo.model.UpdatePointResult;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.kins.vc.constants.Constants;
import com.kins.vs.model.userconference.UserConference;
import com.ltu.fm.configuration.DynamoDBConfiguration;
import com.ltu.fm.dao.AbstractDao;
import com.ltu.fm.exception.DAOException;
import com.ltu.fm.model.user.User;
import com.ltu.fm.utils.AppUtil;


// TODO: Auto-generated Javadoc
/**
 * The Class DDBUserPointDAO.
 */
public class DDBUserPointDAO extends AbstractDao<UserPoint> implements UserPointDAO {
	
	/** The instance. */
	private static DDBUserPointDAO instance = null;
	
	/** The geo data manager. */
	private static GeoDataManager geoDataManager = null;
	
	/** The config. */
	private static GeoDataManagerConfiguration config = null;
	
	/** The mapper. */
	private static ObjectMapper mapper;
	
	/** The factory. */
	private static JsonFactory factory;
	
	/**
	 * Gets the single instance of DDBUserPointDAO.
	 *
	 * @return single instance of DDBUserPointDAO
	 */
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

	/**
	 * Instantiates a new DDB user point dao.
	 */
	protected DDBUserPointDAO() {
		super(UserPoint.class);
	}
	
	/* (non-Javadoc)
	 * @see com.ltu.fm.geo.UserPointDAO#putUser(com.ltu.fm.model.user.User)
	 */
	@Override
	public PutPointResult putUser(User user) throws DAOException {
		try {
			GeoPoint geoPoint = new GeoPoint(user.getLat(),user.getLng());
			AttributeValue rangeKeyAttributeValue = new AttributeValue().withS(user.getId());
			AttributeValue schoolNameKeyAttributeValue = new AttributeValue().withS(user.getDisplayName());
			AttributeValue createdAtAtt = new AttributeValue().withS(AppUtil.getUTCCurTime());

			PutPointRequest putPointRequest = new PutPointRequest(geoPoint, rangeKeyAttributeValue);
			putPointRequest.getPutItemRequest().addItemEntry("displayName", schoolNameKeyAttributeValue);
			putPointRequest.getPutItemRequest().addItemEntry("createdAt", createdAtAtt);

			PutPointResult putPointResult = geoDataManager.putPoint(putPointRequest);
			return putPointResult;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOException("Error puting point", e);
		}
	}
	
	private void deletePoint(String userId) throws IOException, JSONException {
		GeoPoint geoPoint = new GeoPoint(requestObject.getDouble("lat"), requestObject.getDouble("lng"));
		AttributeValue rangeKeyAttributeValue = new AttributeValue().withS(userId);

		DeletePointRequest deletePointRequest = new DeletePointRequest(geoPoint, rangeKeyAttributeValue);
		DeletePointResult deletePointResult = geoDataManager.deletePoint(deletePointRequest);

		printDeletePointResult(deletePointResult, out);
	}
	
	/**
	 * Update user.
	 *
	 * @param user the user
	 * @return the update point result
	 * @throws DAOException the DAO exception
	 */
	public UpdatePointResult updateUser(User user) throws DAOException {
		try {
			GeoPoint geoPoint = new GeoPoint(user.getLat(),user.getLng());
			AttributeValue rangeKeyAttributeValue = new AttributeValue().withS(user.getId());
			AttributeValue createdAtAtt = new AttributeValue().withS(AppUtil.getUTCCurTime());

			UpdatePointRequest updatePointRequest = new UpdatePointRequest(geoPoint, rangeKeyAttributeValue);
			updatePointRequest.getUpdateItemRequest().addKeyEntry("createdAt", createdAtAtt);

			UpdatePointResult updatePointResult = geoDataManager.updatePoint(updatePointRequest);
			return updatePointResult;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOException("Error puting point", e);
		}
	}
	
	/**
	 * Gets the point.
	 *
	 * @param userId the user id
	 * @param lat the lat
	 * @param lng the lng
	 * @return the point
	 */
	public GetPointResult getPoint(String userId, double lat, double lng) {
		GeoPoint geoPoint = new GeoPoint(lat, lng);
		AttributeValue rangeKeyAttributeValue = new AttributeValue().withS(userId);

		GetPointRequest getPointRequest = new GetPointRequest(geoPoint, rangeKeyAttributeValue);
		return geoDataManager.getPoint(getPointRequest);

	}
	
	/* (non-Javadoc)
	 * @see com.ltu.fm.geo.UserPointDAO#queryRadius(com.amazonaws.geo.model.GeoPoint, double, java.lang.Integer, java.lang.String)
	 */
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
	
	public QueryResultPage<UserPoint> findByUserId(String createdAt, Integer limit, String cursor) {
		
		HashMap<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":createdAt", new AttributeValue().withS(createdAt));
		DynamoDBQueryExpression<UserPoint> queryExpression = new DynamoDBQueryExpression<UserPoint>()
				.withIndexName(DynamoDBConfiguration.CREATED_AT_USER_POINT_INDEX)
				.withConsistentRead(false)
				.withKeyConditionExpression("createdAt = :createdAt")
				.withExpressionAttributeValues(eav).withLimit(limit);
		queryExpression.setScanIndexForward(false);
		queryExpression.setExclusiveStartKey(buildExclusiveStartKeyWithUserId(cursor));

		QueryResultPage<UserConference> list = getMapper().queryPage(UserConference.class, queryExpression);

		return list;
	}
	
	private Map<String, AttributeValue> buildExclusiveStartKeyWithCreatedAt(String cursor) {
		if (cursor == null || cursor.trim().equals(Constants.EMPTY_STRING)) {
			return null;
		}
		
		UserPoint item = find(cursor);
		if (item == null) {
			return null;
		}
		
		Map<String, AttributeValue> exclusiveStartKey = new HashMap<String, AttributeValue>();
		exclusiveStartKey.put("id", new AttributeValue(item.));
		exclusiveStartKey.put("userId", new AttributeValue(item.getUserId()));
		exclusiveStartKey.put("startDate", new AttributeValue(AppUtil.toString(item.getStartDate())));
		return exclusiveStartKey;
	}
	
	/**
	 * To user point.
	 *
	 * @param item the item
	 * @return the user point
	 * @throws DAOException the DAO exception
	 */
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
