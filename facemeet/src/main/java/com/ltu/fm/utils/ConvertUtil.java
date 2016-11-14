package com.ltu.fm.utils;

import java.util.Map;

import org.apache.log4j.Logger;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.ltu.fm.model.user.User;

/**
 * The Class ConvertUtil.
 * 
 * @author uyphu
 */
public class ConvertUtil {

	/** The log. */
	private static Logger log = Logger.getLogger(ConvertUtil.class);

	public static User toUser(Map<String, AttributeValue> item) {
		User user = new User();
		try {
			user.setId(item.get("id").getS());
			user.setEmail(item.get("email") != null ? item.get("email").getS() : null);
			user.setPassword(item.get("password") != null ? item.get("password").getB() : null);
			user.setSalt(item.get("salt") != null ? item.get("salt").getB() : null);
			user.setType(item.get("type") != null ? item.get("type").getS() : null);
			user.setDisplayName(item.get("displayName") != null ? item.get("displayName").getS() : null);
			user.setActivateCode(item.get("activateCode") != null ? item.get("activateCode").getS() : null);
			user.setImageUrl(item.get("imageUrl") != null ? item.get("imageUrl").getS() : null);
			user.setStatus(item.get("status").getS());
			user.setCreatedAt(item.get("createdAt") != null ? AppUtil.toDate(item.get("createdAt").getS()) : null);
			user.setCognitoIdentityId(item.get("identityId") != null ? item.get("identityId").getS() : null);
		} catch (Exception e) {
			log.error(e.getMessage(), e.getCause());
		}
		return user;
	}
	
}

