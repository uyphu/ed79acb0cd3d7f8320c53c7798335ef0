package com.kins.vs.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.log4j.Logger;

import com.ltu.fm.configuration.ExceptionMessages;
import com.ltu.fm.constants.Constants;
import com.ltu.fm.exception.BadRequestException;
import com.ltu.fm.model.user.User;

/**
 * The Class GoogleService.
 * @author uyphu
 */
public class GoogleService {
	
	/** The log. */
	private final Logger log = Logger.getLogger(GoogleService.class);
	
	/** The instance. */
	private static GoogleService instance = null;
	 
	/**
	 * Gets the single instance of ContactService.
	 *
	 * @return single instance of ContactService
	 */
	public static GoogleService getInstance() {
		if (instance == null) {
			synchronized (GoogleService.class) {
				if (instance == null) {
					instance = new GoogleService();
				}
			}
		}
		return instance;
	}
	
	/**
	 * Authenticate.
	 *
	 * @return the string
	 */
	public User authenticate(String token) throws BadRequestException{
		User user = new User();
		try {
			String address = "https://www.googleapis.com/oauth2/v2/tokeninfo?access_token=" + token;

			URL url = new URL(address);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			
			String strEmail = String.valueOf("\"email\": \"");
			
			String str;
			while ((str = in.readLine()) != null) {
				str = str.trim();
				if (str.startsWith(strEmail)) {
					user.setEmail(str.substring(strEmail.length(), str.length() - 2));
					
				}
			}
			user.setType(Constants.GOOGLE_TYPE);
			in.close();
			
		} catch (Exception e) {
			log.error(e.getMessage(), e.getCause());
			throw new BadRequestException(ExceptionMessages.EX_AUTHENTICATE_GOOGLE);
		}
		
		return user;
	}

}
