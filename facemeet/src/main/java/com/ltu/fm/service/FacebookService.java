package com.ltu.fm.service;

import org.apache.log4j.Logger;

import com.ltu.fm.configuration.ExceptionMessages;
import com.ltu.fm.exception.BadRequestException;
import com.ltu.fm.exception.CommonException;
import com.ltu.fm.helper.UserHelper;
import com.ltu.fm.model.user.User;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;

/**
 * The Class FBGraph.
 * @author uyphu
 */
public class FacebookService {

	/** The log. */
	private final Logger log = Logger.getLogger(FacebookService.class);
	
	/** The instance. */
	private static FacebookService instance = null;
	 
	/**
	 * Gets the single instance of ContactService.
	 *
	 * @return single instance of ContactService
	 */
	public static FacebookService getInstance() {
		if (instance == null) {
			//synchronized (FacebookService.class) {
				if (instance == null) {
					instance = new FacebookService();
				}
			//}
		}
		return instance;
	}

	/**
	 * Authenticate.
	 *
	 * @return the user
	 * @throws CommonException the common exception
	 */
	public User authenticate(String token) throws BadRequestException{
		try {
			FacebookClient facebookClient = new DefaultFacebookClient(token, Version.VERSION_2_7);
			com.restfb.types.User user = facebookClient.fetchObject("me", com.restfb.types.User.class, Parameter.with("fields", "email,first_name,last_name,gender"));
			return UserHelper.convertToUser(user);
		} catch (Exception e) {
			log.error(e.getMessage(), e.getCause());
			throw new BadRequestException(ExceptionMessages.EX_AUTHENTICATE_FACEBOOK);
		}
	}

}
