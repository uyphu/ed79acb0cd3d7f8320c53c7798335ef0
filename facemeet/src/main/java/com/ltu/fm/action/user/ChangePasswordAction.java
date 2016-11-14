package com.ltu.fm.action.user;

import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.JsonObject;
import com.ltu.fm.action.AbstractLambdaAction;
import com.ltu.fm.auth.Token;
import com.ltu.fm.auth.TokenProvider;
import com.ltu.fm.configuration.ExceptionMessages;
import com.ltu.fm.dao.factory.DAOFactory;
import com.ltu.fm.exception.BadRequestException;
import com.ltu.fm.exception.DAOException;
import com.ltu.fm.exception.InternalErrorException;
import com.ltu.fm.helper.PasswordHelper;
import com.ltu.fm.model.action.user.ChangePasswordRequest;
import com.ltu.fm.model.action.user.LoginUserResponse;
import com.ltu.fm.model.user.User;
import com.ltu.fm.model.user.UserDAO;

/**
 * Action that creates a new User
 * <p/>
 * POST to /pets/
 */
public class ChangePasswordAction extends AbstractLambdaAction{
	
	/** The logger. */
	private LambdaLogger logger;

    public String handle(JsonObject request, Context lambdaContext) throws BadRequestException, InternalErrorException {
        logger = lambdaContext.getLogger();
        
        ChangePasswordRequest input = getGson().fromJson(request, ChangePasswordRequest.class);
        validateInput(input);
        
        LoginUserResponse output = changePassword(input);
        return getGson().toJson(output);
    }

	private void validateInput(ChangePasswordRequest input) throws BadRequestException {
		if (input == null) {
            throw new BadRequestException(ExceptionMessages.EX_INVALID_INPUT);
        }
        
        if (input.getEmail() == null ||
                input.getEmail().trim().equals("")) {
            throw new BadRequestException(ExceptionMessages.EX_PARAM_EMAIL_REQUIRED);
        }
        
        if (input.getPassword() == null ||
                input.getPassword().trim().equals("")) {
            throw new BadRequestException(ExceptionMessages.EX_PARAM_PASSWORD_REQUIRED);
        }
        
        if (input.getChangeKey() == null ||
                input.getChangeKey().trim().equals("")) {
            throw new BadRequestException(ExceptionMessages.EX_PARAM_CHANGE_KEY_REQUIRED);
        }
	}
    
    private LoginUserResponse changePassword(ChangePasswordRequest input) throws BadRequestException, InternalErrorException {
    	UserDAO dao = DAOFactory.getUserDAO();
		User user = dao.findByEmail(input.getEmail());
		try {
			if (user == null) {
				throw new BadRequestException(ExceptionMessages.EX_USER_NOT_FOUND);
			}
			if (!input.getChangeKey().equals(user.getActivateCode())) {
				throw new BadRequestException(ExceptionMessages.EX_CHANGE_KEY_INVALID);
			}
			
			byte[] salt = PasswordHelper.generateSalt();
            byte[] encryptedPassword = PasswordHelper.getEncryptedPassword(input.getPassword(), salt);
            user.setPassword(ByteBuffer.wrap(encryptedPassword));
            user.setSalt(ByteBuffer.wrap(salt));
            user.setActivateCode(null);
            dao.update(user);
            
            TokenProvider provider = TokenProvider.getInstance();
            Token token = provider.createToken(user);
        	LoginUserResponse output = new LoginUserResponse();
        	output.setExpires(token.getExpires());
        	output.setToken(token.getToken());
        	output.setType(token.getType());
        	output.setItem(user);
        	return output;
			
		} catch (final NoSuchAlgorithmException e) {
            logger.log("No algrithm found for password encryption\n" + e.getMessage());
            throw new InternalErrorException(ExceptionMessages.EX_PWD_SALT);
        } catch (final InvalidKeySpecException e) {
            logger.log("No KeySpec found for password encryption\n" + e.getMessage());
            throw new InternalErrorException(ExceptionMessages.EX_PWD_ENCRYPT);
        } catch (final DAOException e) {
        	logger.log("Error changing password " + e.getMessage());
			throw new InternalErrorException(ExceptionMessages.EX_CHANGE_PASSWORD);
		} 

	}

}

