package com.kins.vs.model.device;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.ltu.fm.configuration.DynamoDBConfiguration;
import com.ltu.fm.configuration.ExceptionMessages;
import com.ltu.fm.constants.Constants;
import com.ltu.fm.dao.AbstractDao;
import com.ltu.fm.exception.DAOException;
import com.ltu.fm.exception.ErrorCodeDetail;
import com.ltu.fm.utils.AppUtil;

/**
 * The Class DDBDeviceDAO.
 * 
 * @author uyphu
 */
public class DDBDeviceDAO extends AbstractDao<Device> implements DeviceDAO {

	/** The instance. */
	private static DDBDeviceDAO instance = null;

	/**
	 * Returns an initialized instance of the DDBDeviceDAO object. DAO objects
	 * should be retrieved through the DAOFactory class
	 *
	 * @return An initialized instance of the DDBDeviceDAO object
	 */
	public static DDBDeviceDAO getInstance() {
		if (instance == null) {
			instance = new DDBDeviceDAO();
		}
		return instance;
	}

	/**
	 * Instantiates a new DDB device dao.
	 */
	protected DDBDeviceDAO() {
		super(Device.class);
	}

	@Override
	public Device insert(Device device) throws DAOException {
		Device item = findByPhoneId(device.getPhoneId());
		if (item != null) {
			throw new DAOException(ExceptionMessages.EX_DUPLICATED_ITEM);
		}
		device.setCreatedAt(AppUtil.getUTCDateTime());
		return super.save(device);
	}

	@Override
	public Device update(Device device) throws DAOException {
		if (device.getId() == null) {
			throw new DAOException(ExceptionMessages.EX_DEVICE_NOT_FOUND);
		}
		//FIXME need to remove device.setCreatedAt(C); 
		device.setCreatedAt(AppUtil.getUTCDateTime());
		return super.save(device);
	}

	@Override
	public Device merge(Device device) throws DAOException {
		if (device.getId() != null) {
			return update(device);
		} else {
			Device old = findByPhoneId(device.getPhoneId());
			if (old != null ) {
				device.setId(old.getId());
				return update(device);
			}
			return insert(device);
		}
	}

	@Override
	public void delete(String id) throws DAOException {
		Device item = get(id);
		if (item == null) {
			throw new DAOException(ErrorCodeDetail.ERROR_RECORD_NOT_FOUND.getMsg());
		} else {
			super.delete(item);
		}
	}

	@Override
	public Device find(String id) {
		return super.find(id);
	}

	@Override
	public List<Device> search(String query, Integer limit, String cursor) {
		if (query == null) {
			return mapperScan(query, limit, cursor);
		}
		return scan(query, limit, cursor);
	}
	
	private Map<String, AttributeValue> buildExclusiveStartKey(String cursor) {
		if (cursor == null || cursor.trim().equals(Constants.EMPTY_STRING)) {
			return null;
		}
		Map<String, AttributeValue> exclusiveStartKey = new HashMap<String, AttributeValue>();
		exclusiveStartKey.put("id", new AttributeValue(cursor));
		return exclusiveStartKey;
	}

	public Device findByPhoneId(String phoneId) {
		QueryResultPage<Device> list = findByPhoneId(phoneId, 1, null);
		if (!list.getResults().isEmpty()) {
			return list.getResults().get(0);
		}
		return null;
	}
	
	private ScanRequest buildScan(String query, int limit) {
		ScanRequest scanRequest = new ScanRequest(DynamoDBConfiguration.DEVICE_TABLE_NAME);

		if (query != null) {
			HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
			String[] fields = query.split(Constants.AND_STRING);
			for (String field : fields) {
				if (field.indexOf("phoneId:") != -1) {
					String[] array = field.split(":");
					Condition condition = new Condition().withComparisonOperator(ComparisonOperator.EQ.toString())
							.withAttributeValueList(new AttributeValue().withS(array[1]));
					scanFilter.put("phoneId", condition);
				} else if (field.indexOf("type:") != -1) {
					String[] array = field.split(":");
					Condition condition = new Condition().withComparisonOperator(ComparisonOperator.EQ.toString())
							.withAttributeValueList(new AttributeValue().withS(array[1]));
					scanFilter.put("type", condition);
				} else if (field.indexOf("lastLoginUserId:") != -1) {
					String[] array = field.split(":");
					Condition condition = new Condition().withComparisonOperator(ComparisonOperator.EQ.toString())
							.withAttributeValueList(new AttributeValue().withS(array[1]));
					scanFilter.put("lastLoginUserId", condition);
				}
			}

			scanRequest.withScanFilter(scanFilter);
		}
		scanRequest.setLimit(DynamoDBConfiguration.SCAN_LIMIT);
        
		return scanRequest;
	}
	
	private DynamoDBScanExpression buildScanMapper(String query, Integer limit) {
		DynamoDBScanExpression dbScanExpression = new DynamoDBScanExpression();

		if (query != null) {
			String[] fields = query.split(Constants.AND_STRING);
			for (String field : fields) {
				if (field.indexOf("phoneId:") != -1) {
					String[] array = field.split(":");
					Condition condition = new Condition().withComparisonOperator(ComparisonOperator.EQ.toString())
							.withAttributeValueList(new AttributeValue().withS(array[1]));
					dbScanExpression.addFilterCondition("phoneId", condition);
				} else if (field.indexOf("type:") != -1) {
					String[] array = field.split(":");
					Condition condition = new Condition().withComparisonOperator(ComparisonOperator.EQ.toString())
							.withAttributeValueList(new AttributeValue().withS(array[1]));
					dbScanExpression.addFilterCondition("type", condition);
				} else if (field.indexOf("lastLoginUserId:") != -1) {
					String[] array = field.split(":");
					Condition condition = new Condition().withComparisonOperator(ComparisonOperator.EQ.toString())
							.withAttributeValueList(new AttributeValue().withS(array[1]));
					dbScanExpression.addFilterCondition("lastLoginUserId", condition);
				}
			}
		}
//		if (limit <= 0 || limit > DynamoDBConfiguration.SCAN_LIMIT) {
//			limit = DynamoDBConfiguration.SCAN_LIMIT;
//		}
//		dbScanExpression.setLimit(limit);
		
		dbScanExpression.setLimit(DynamoDBConfiguration.SCAN_LIMIT);
        
		return dbScanExpression;
	}
	
	private DynamoDBQueryExpression<Device> buildQueryMapper(String query, Integer limit) {
		DynamoDBQueryExpression<Device> dbQueryExpression = new DynamoDBQueryExpression<Device>();

		if (query != null) {
			String[] fields = query.split(Constants.AND_STRING);
			for (String field : fields) {
				if (field.indexOf("phoneId:") != -1) {
					String[] array = field.split(":");
					Condition condition = new Condition().withComparisonOperator(ComparisonOperator.EQ.toString())
							.withAttributeValueList(new AttributeValue().withS(array[1]));
					dbQueryExpression.withQueryFilterEntry("phoneId", condition);
				}
			}
		}
		//FIXME phuLTU needs to review this
//		if (limit <= 0 || limit > DynamoDBConfiguration.SCAN_LIMIT) {
//			limit = DynamoDBConfiguration.SCAN_LIMIT;
//		}
//		dbQueryExpression.withLimit(limit);
		dbQueryExpression.withLimit(DynamoDBConfiguration.SCAN_LIMIT);
		return dbQueryExpression;
	}

	@Override
	public List<Device> scan(String query, Integer limit, String cursor) {
		int count = limit != null ? limit : 0;
		ScanRequest scanRequest = buildScan(query, count);
		Map<String, AttributeValue> exclusiveStartKey = buildExclusiveStartKey(cursor);
		List<Device> devices = new ArrayList<Device>();
		
		do {
			if (exclusiveStartKey != null) {
				scanRequest.setExclusiveStartKey(exclusiveStartKey);
			}
			ScanResult scanResult = client.scan(scanRequest);
			
			if (scanResult != null && scanResult.getItems().size() > 0) {
				for (Map<String, AttributeValue> item : scanResult.getItems()) {
					devices.add(new Device(item));
					if (count == devices.size()) {
						return devices;
					}
				}
			}
			exclusiveStartKey = scanResult.getLastEvaluatedKey();
		} while (exclusiveStartKey != null);
		
		return devices;
	}

	@Override
	public List<Device> mapperScan(String query, int limit, String cursor) {
		DynamoDBScanExpression scanExpression = buildScanMapper(query, limit);
		Map<String, AttributeValue> exclusiveStartKey = buildExclusiveStartKey(cursor);
		if (exclusiveStartKey != null) {
			scanExpression.setExclusiveStartKey(exclusiveStartKey);
		}
		PaginatedScanList<Device> scanList = getMapper().scan(Device.class, scanExpression);
		return scanList;
	}
	
	@Override
	public List<Device> mapperQuery(String query, int limit, String cursor) {
		//FIXME PhuLTU Illegal query expression: No hash key condition is found in the query
		DynamoDBQueryExpression<Device> dbQueryExpression = buildQueryMapper(query, limit);
		Map<String, AttributeValue> exclusiveStartKey = buildExclusiveStartKey(cursor);
		if (exclusiveStartKey != null) {
			dbQueryExpression.setExclusiveStartKey(exclusiveStartKey);
		}
		return getMapper().query(Device.class, dbQueryExpression);
	}
	
	private Map<String, AttributeValue> buildExclusiveStartKeyWithUserId(String cursor) {
		if (cursor == null || cursor.trim().equals(Constants.EMPTY_STRING)) {
			return null;
		}
		
		Device item = find(cursor);
		if (item == null) {
			return null;
		}
		
		Map<String, AttributeValue> exclusiveStartKey = new HashMap<String, AttributeValue>();
		exclusiveStartKey.put("id", new AttributeValue(item.getId()));
		exclusiveStartKey.put("lastLoginUserId", new AttributeValue(item.getLastLoginUserId()));
		return exclusiveStartKey;
	}
	
	public QueryResultPage<Device> findByUserId(String userId, Integer limit, String cursor) {
		
		HashMap<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":lastLoginUserId", new AttributeValue().withS(userId));
		DynamoDBQueryExpression<Device> queryExpression = new DynamoDBQueryExpression<Device>()
				.withIndexName("lastLoginUserId-index")
				.withConsistentRead(false)
				.withKeyConditionExpression("lastLoginUserId = :lastLoginUserId")
				.withExpressionAttributeValues(eav).withLimit(limit);
		queryExpression.setExclusiveStartKey(buildExclusiveStartKeyWithUserId(cursor));

		QueryResultPage<Device> list = getMapper().queryPage(Device.class, queryExpression);

		return list;
	}
	
	private Map<String, AttributeValue> buildExclusiveStartKeyWithPhoneId(String cursor) {
		if (cursor == null || cursor.trim().equals(Constants.EMPTY_STRING)) {
			return null;
		}
		
		Device item = find(cursor);
		if (item == null) {
			return null;
		}
		
		Map<String, AttributeValue> exclusiveStartKey = new HashMap<String, AttributeValue>();
		exclusiveStartKey.put("id", new AttributeValue(item.getId()));
		exclusiveStartKey.put("phoneId", new AttributeValue(item.getPhoneId()));
		return exclusiveStartKey;
	}
	
	public QueryResultPage<Device> findByPhoneId(String phoneId, Integer limit, String cursor) {
		
		HashMap<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":phoneId", new AttributeValue().withS(phoneId));
		DynamoDBQueryExpression<Device> queryExpression = new DynamoDBQueryExpression<Device>()
				.withIndexName("phoneId-index")
				.withConsistentRead(false)
				.withKeyConditionExpression("phoneId = :phoneId")
				.withExpressionAttributeValues(eav).withLimit(limit);
		queryExpression.setExclusiveStartKey(buildExclusiveStartKeyWithPhoneId(cursor));

		QueryResultPage<Device> list = getMapper().queryPage(Device.class, queryExpression);

		return list;
	}
	
	/**
	 * Gets the mapper.
	 *
	 * @return the mapper
	 */
	protected DynamoDBMapper getMapper() {
        return new DynamoDBMapper(client);
    }

}
