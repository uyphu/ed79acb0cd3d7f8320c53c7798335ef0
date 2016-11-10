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
package com.ltu.fm.model.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
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
 * DynamoDB implementation of the UserDAO interface. This class reads the
 * configuration from the DyanmoDBConfiguration object in the
 * com.amazonaws.apigatewaydemo.configuration package. Credentials to access
 * DynamoDB are retrieved from the Lambda environment.
 * <p/>
 * The table in DynamoDB should be created with an Hash Key called username.
 */
public class DDBUserDAO extends AbstractDao<User> implements UserDAO {
	private static DDBUserDAO instance = null;

	// credentials for the client come from the environment variables
	// pre-configured by Lambda. These are tied to the
	// Lambda function execution role.
	// private static AmazonDynamoDBClient ddbClient = new
	// AmazonDynamoDBClient();//AmazonDynamoDBClientUtil.getInstance();
	// static AmazonDynamoDBClient client =
	// AmazonDynamoDBClientUtil.getInstance();

	/**
	 * Returns an initialized instance of the DDBUserDAO object. DAO objects
	 * should be retrieved through the DAOFactory class
	 *
	 * @return An initialized instance of the DDBUserDAO object
	 */
	public static DDBUserDAO getInstance() {
		if (instance == null) {
			instance = new DDBUserDAO();
		}

		return instance;
	}

	protected DDBUserDAO() {
		super(User.class);
	}

	/**
	 * Queries DynamoDB to find a user by its Username
	 *
	 * @param username
	 *            The username to search for
	 * @return A populated User object, null if the user was not found
	 * @throws DAOException
	 */
	public User getUserByEmail(String email) throws DAOException {
		if (email == null || email.trim().equals("")) {
			throw new DAOException("Cannot lookup null or empty user");
		}

		return findByEmail(email);
	}

	/**
	 * Inserts a new row in the DynamoDB users table.
	 *
	 * @param user
	 *            The new user information
	 * @return The username that was just inserted in DynamoDB
	 * @throws DAOException
	 */
	public String createUser(User user) throws DAOException {
		if (user.getEmail() == null || user.getEmail().trim().equals("")) {
			throw new DAOException("Cannot create user with empty username");
		}

		return insert(user).getId();
	}

	@Override
	public User insert(User user) throws DAOException {
		User item = findByEmail(user.getEmail());
		if (item != null) {
			throw new DAOException(ExceptionMessages.EX_DUPLICATED_ITEM);
		}
		user.setCreatedAt(AppUtil.getUTCDateTime());
		return super.save(user);
	}

	@Override
	public User update(User user) throws DAOException {
		if (user.getId() == null) {
			throw new DAOException(ExceptionMessages.EX_INVALID_INPUT);
		}
		return super.save(user);
	}

	@Override
	public User merge(User user) throws DAOException {
		if (user.getId() != null) {
			return update(user);
		} else {
			return insert(user);
		}
	}

	@Override
	public void delete(String id) throws DAOException {
		User item = get(id);
		if (item == null) {
			throw new DAOException(ErrorCodeDetail.ERROR_RECORD_NOT_FOUND.getMsg());
		} else {
			super.delete(item);
		}
	}

	@Override
	public User find(String id) {
		return super.find(id);
	}

	@Override
	public List<User> search(String query, int limit, String cursor) {
		if (query == null) {
			return mapperScan(query, limit, cursor);
		}
		return scan(query, limit, cursor);
	}

	public User findByEmail(String email) {
		List<User> list = search("email:" + email, 1, null);
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public User findByActivateCode(String activateCode) throws DAOException {
		List<User> list = search("activateCode:" + activateCode, 1, null);
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public User activateUser(String activateCode) throws DAOException {
		List<User> list = search("activateCode:" + activateCode, 1, null);
		if (list != null && !list.isEmpty()) {
			User user = list.get(0);
			user.setActivateCode(null);
			user.setStatus(Constants.YES_STATUS);
			return update(user);
		}
		return null;
	}

	private Map<String, AttributeValue> buildExclusiveStartKey(String cursor) {
		if (cursor == null || cursor.trim().equals(Constants.EMPTY_STRING)) {
			return null;
		}
		Map<String, AttributeValue> exclusiveStartKey = new HashMap<String, AttributeValue>();
		exclusiveStartKey.put("id", new AttributeValue(cursor));
		return exclusiveStartKey;
	}

	private ScanRequest buildScan(String query, int limit) {
		ScanRequest scanRequest = new ScanRequest(DynamoDBConfiguration.USERS_TABLE_NAME);

		if (query != null) {
			HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
			String[] fields = query.split(Constants.AND_STRING);
			for (String field : fields) {
				if (field.indexOf("email:") != -1) {
					String[] array = field.split(":");
					Condition condition = new Condition().withComparisonOperator(ComparisonOperator.EQ.toString())
							.withAttributeValueList(new AttributeValue().withS(array[1]));
					scanFilter.put("email", condition);
				} else if (field.indexOf("pmCode:") != -1) {
					String[] array = field.split(":");
					Condition condition = new Condition().withComparisonOperator(ComparisonOperator.EQ.toString())
							.withAttributeValueList(new AttributeValue().withS(array[1]));
					scanFilter.put("pmCode", condition);
				} else if (field.indexOf("activateCode:") != -1) {
					String[] array = field.split(":");
					Condition condition = new Condition().withComparisonOperator(ComparisonOperator.EQ.toString())
							.withAttributeValueList(new AttributeValue().withS(array[1]));
					scanFilter.put("activateCode", condition);
				} else if (field.indexOf("status:") != -1) {
					String[] array = field.split(":");
					Condition condition = new Condition().withComparisonOperator(ComparisonOperator.EQ.toString())
							.withAttributeValueList(new AttributeValue().withS(array[1]));
					scanFilter.put("status", condition);
				} else if (field.indexOf("type:") != -1) {
					String[] array = field.split(":");
					Condition condition = new Condition().withComparisonOperator(ComparisonOperator.EQ.toString())
							.withAttributeValueList(new AttributeValue().withS(array[1]));
					scanFilter.put("type", condition);
				}
			}

			scanRequest.withScanFilter(scanFilter);
		}
		// if (limit <= 0 || limit > DynamoDBConfiguration.SCAN_LIMIT) {
		// limit = DynamoDBConfiguration.SCAN_LIMIT;
		// }
		// scanRequest.setLimit(limit);

		scanRequest.setLimit(DynamoDBConfiguration.SCAN_LIMIT);

		return scanRequest;
	}

	private DynamoDBScanExpression buildScanMapper(String query, int limit) {
		DynamoDBScanExpression dbScanExpression = new DynamoDBScanExpression();

		if (query != null) {
			String[] fields = query.split(Constants.AND_STRING);
			for (String field : fields) {
				if (field.indexOf("email:") != -1) {
					String[] array = field.split(":");
					Condition condition = new Condition().withComparisonOperator(ComparisonOperator.EQ.toString())
							.withAttributeValueList(new AttributeValue().withS(array[1]));
					dbScanExpression.addFilterCondition("email", condition);
				} else if (field.indexOf("pmCode:") != -1) {
					String[] array = field.split(":");
					Condition condition = new Condition().withComparisonOperator(ComparisonOperator.EQ.toString())
							.withAttributeValueList(new AttributeValue().withS(array[1]));
					dbScanExpression.addFilterCondition("pmCode", condition);
				} else if (field.indexOf("activateCode:") != -1) {
					String[] array = field.split(":");
					Condition condition = new Condition().withComparisonOperator(ComparisonOperator.EQ.toString())
							.withAttributeValueList(new AttributeValue().withS(array[1]));
					dbScanExpression.addFilterCondition("activateCode", condition);
				} else if (field.indexOf("status:") != -1) {
					String[] array = field.split(":");
					Condition condition = new Condition().withComparisonOperator(ComparisonOperator.EQ.toString())
							.withAttributeValueList(new AttributeValue().withS(array[1]));
					dbScanExpression.addFilterCondition("status", condition);
				} else if (field.indexOf("type:") != -1) {
					String[] array = field.split(":");
					Condition condition = new Condition().withComparisonOperator(ComparisonOperator.EQ.toString())
							.withAttributeValueList(new AttributeValue().withS(array[1]));
					dbScanExpression.addFilterCondition("type", condition);
				}
			}
		}
		// if (limit <= 0 || limit > DynamoDBConfiguration.SCAN_LIMIT) {
		// limit = DynamoDBConfiguration.SCAN_LIMIT;
		// }
		// dbScanExpression.setLimit(limit);

		dbScanExpression.setLimit(DynamoDBConfiguration.SCAN_LIMIT);

		return dbScanExpression;
	}

	@Override
	public List<User> scan(String query, int limit, String cursor) {
		ScanRequest scanRequest = buildScan(query, limit);
		Map<String, AttributeValue> exclusiveStartKey = buildExclusiveStartKey(cursor);
		List<User> users = new ArrayList<User>();

		do {
			if (exclusiveStartKey != null) {
				scanRequest.setExclusiveStartKey(exclusiveStartKey);
			}
			ScanResult scanResult = client.scan(scanRequest);

			if (scanResult != null && scanResult.getItems().size() > 0) {
				for (Map<String, AttributeValue> item : scanResult.getItems()) {
					users.add(new User(item));
					if (limit == users.size()) {
						return users;
					}
				}
			}
			exclusiveStartKey = scanResult.getLastEvaluatedKey();
		} while (exclusiveStartKey != null);

		return users;
	}

	@Override
	public List<User> mapperScan(String query, int limit, String cursor) {
		DynamoDBScanExpression scanExpression = buildScanMapper(query, limit);
		Map<String, AttributeValue> exclusiveStartKey = buildExclusiveStartKey(cursor);
		if (exclusiveStartKey != null) {
			scanExpression.setExclusiveStartKey(exclusiveStartKey);
		}
		PaginatedScanList<User> scanList = getMapper().scan(User.class, scanExpression);
		return scanList;
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
