package com.ltu.fm.auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

import com.ltu.fm.constants.Constants;
import com.ltu.fm.dao.factory.DAOFactory;
import com.ltu.fm.exception.DAOException;
import com.ltu.fm.model.user.User;
import com.ltu.fm.model.user.UserDAO;

/**
 * The Class TokenProvider.
 * @author uyphu
 */
public class TokenProvider {
	
    /** The secret key. */
    private final String secretKey;
    
    /** The token validity. */
    private final int tokenValidity;
    
    /** The instance. */
	private static TokenProvider instance = null;
	
	/**
	 * Gets the single instance of TokenProvider.
	 *
	 * @param secretKey the secret key
	 * @param tokenValidity the token validity
	 * @return single instance of TokenProvider
	 */
	public static TokenProvider getInstance(String secretKey, int tokenValidity) {
		if (instance == null) {
			instance = new TokenProvider(secretKey, tokenValidity);
		}
		return instance;
	}
	
	/**
	 * Gets the single instance of TokenProvider.
	 *
	 * @return single instance of TokenProvider
	 */
	public static TokenProvider getInstance() {
		if (instance == null) {
			instance = new TokenProvider(Constants.SECRET_KEY, Constants.TOKEN_VALIDITY_IN_SECONDS);
		}
		return instance;
	}

    /**
     * Instantiates a new token provider.
     *
     * @param secretKey the secret key
     * @param tokenValidity the token validity
     */
    public TokenProvider(String secretKey, int tokenValidity) {
        this.secretKey = secretKey;
        this.tokenValidity = tokenValidity;
    }

    /**
     * Creates the token.
     *
     * @param user the user
     * @return the token
     */
    public Token createToken(User user) {
        long expires = System.currentTimeMillis() + 1000L * tokenValidity;
        String token = user.getId() + ":" + expires + ":" + computeSignature(user, expires);
        return new Token(token, expires, user.getType());
    }
    
    /**
     * Creates the token.
     *
     * @param token the token
     * @return the string
     */
    public String createToken(String token) throws DAOException {
    	//long expires = System.currentTimeMillis() + 1000L * tokenValidity;
        String[] parts = token.split(":");
    	//UserService userService = UserService.getInstance();
    	UserDAO dao = DAOFactory.getUserDAO();
    	User user = dao.find(parts[0]);
    	String signature = parts[2];
    	long expires = Long.parseLong(parts[1]);
    	String signatureToMatch = computeSignature(user, expires);
    	
    	if (signature.equals(signatureToMatch)) {
    		expires = System.currentTimeMillis() + 1000L * tokenValidity;
    		//String tokenStr = user.getEmail() + ":" + expires + ":" + computeSignature(user, expires);
    		String tokenStr = user.getId() + ":" + expires + ":" + computeSignature(user, expires);
            return new Token(tokenStr, expires, user.getType()).toJSON();
		} 
    	return null;
    }
    
    /**
     * Compute signature.
     *
     * @param user the user
     * @param expires the expires
     * @return the string
     */
    public String computeSignature(User user, long expires) {
        StringBuilder signatureBuilder = new StringBuilder();
        signatureBuilder.append(user.getEmail()).append(":");
        signatureBuilder.append(expires).append(":");
        signatureBuilder.append(user.getPassword()).append(":");
        signatureBuilder.append(secretKey);

        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No MD5 algorithm available!");
        }
        return new String(Hex.encodeHex(digest.digest(signatureBuilder.toString().getBytes())));
    }

    /**
     * Gets the user name from token.
     *
     * @param authToken the auth token
     * @return the user name from token
     */
    public String getUserNameFromToken(String authToken) {
        if (null == authToken) {
            return null;
        }
        String[] parts = authToken.split(":");
        return parts[0];
    }

    /**
     * Validate token.
     *
     * @param authToken the auth token
     * @param user the user
     * @return true, if successful
     */
    public boolean validateToken(String authToken, User user) {
        String[] parts = authToken.split(":");
        long expires = Long.parseLong(parts[1]);
        String signature = parts[2];
        String signatureToMatch = computeSignature(user, expires);
        return expires >= System.currentTimeMillis() && signature.equals(signatureToMatch);
    }
    
    /**
     * Validate token.
     *
     * @param authToken the auth token
     * @return true, if successful
     */
    public boolean validateToken(String authToken) {
    	try {
    		String[] parts = authToken.split(":");
    		UserDAO dao = DAOFactory.getUserDAO();
        	User user = dao.find(parts[0]);
    		long expires = Long.parseLong(parts[1]);
            String signature = parts[2];
            String signatureToMatch = computeSignature(user, expires);
            return expires >= System.currentTimeMillis() && signature.equals(signatureToMatch);
    		
		} catch (Exception e) {
			//log.error(e.getMessage(), e.getCause());
		}
    	return false;
    }
    
    /**
     * Checks if is expired.
     *
     * @param authToken the auth token
     * @return true, if is expired
     */
    public boolean isExpired(String authToken) {
    	try {
    		String[] parts = authToken.split(":");
    		UserDAO dao = DAOFactory.getUserDAO();
        	User user = dao.find(parts[0]);
    		long expires = Long.parseLong(parts[1]);
            String signature = parts[2];
            String signatureToMatch = computeSignature(user, expires);
            if (signature.equals(signatureToMatch)) {
            	if (expires < System.currentTimeMillis()) {
					return true;
				}
            }
		} catch (Exception e) {
			//log.error(e.getMessage(), e.getCause());
		}
    	return false;
    }
    
    /**
     * Checks if is matched.
     *
     * @param authToken the auth token
     * @return true, if is matched
     */
    public boolean isMatched(String authToken) {
    	try {
    		String[] parts = authToken.split(":");
    		UserDAO dao = DAOFactory.getUserDAO();
        	User user = dao.find(parts[0]);
    		long expires = Long.parseLong(parts[1]);
            String signature = parts[2];
            String signatureToMatch = computeSignature(user, expires);
            if (signature.equals(signatureToMatch)) {
            	return true;
            }
		} catch (Exception e) {
			//log.error(e.getMessage(), e.getCause());
		}
    	return false;
    }
    
    
    //FIXME phu
//    public static void main(String[] args) {    	
//    	System.out.println("test...");
//    	TokenProvider tokenProvider = new TokenProvider("mySecretKy", 5);
//    	User user = new User("uyphu@yahoo.com", "12346", Constants.USER_TYPE);
//    	Token token = tokenProvider.createToken(user);
//    	System.out.println(token.toJSONObject());
//    	
//    	boolean valid = tokenProvider.validateToken(token.getToken(), user);
//    	System.out.println(valid);
//    }
    
}
