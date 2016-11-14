package com.ltu.fm.action.user;

import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.JsonObject;
import com.ltu.fm.action.AbstractLambdaAction;
import com.ltu.fm.configuration.ExceptionMessages;
import com.ltu.fm.constants.Constants;
import com.ltu.fm.dao.factory.DAOFactory;
import com.ltu.fm.exception.AuthorizationException;
import com.ltu.fm.exception.BadRequestException;
import com.ltu.fm.exception.CommonException;
import com.ltu.fm.exception.DAOException;
import com.ltu.fm.exception.InternalErrorException;
import com.ltu.fm.helper.PasswordHelper;
import com.ltu.fm.model.action.user.RegisterUserRequest;
import com.ltu.fm.model.action.user.RegisterUserResponse;
import com.ltu.fm.model.user.User;
import com.ltu.fm.model.user.UserDAO;
import com.ltu.fm.model.user.UserIdentity;
import com.ltu.fm.provider.CredentialsProvider;
import com.ltu.fm.provider.ProviderFactory;
import com.ltu.fm.utils.AppUtil;
import com.ltu.fm.utils.MailUtil;
import com.ltu.fm.utils.RandomUtil;

/**
 * Action that creates a new User
 * <p/>
 * POST to /pets/
 */
public class RegisterAction extends AbstractLambdaAction{
	private LambdaLogger logger;
	private CredentialsProvider cognito = ProviderFactory.getCredentialsProvider();

    public String handle(JsonObject request, Context lambdaContext) throws BadRequestException, InternalErrorException {
        logger = lambdaContext.getLogger();
        
        RegisterUserRequest input = getGson().fromJson(request, RegisterUserRequest.class);
        if (input == null ||
                input.getEmail() == null ||
                input.getEmail().trim().equals("")) {
            throw new BadRequestException(ExceptionMessages.EX_PARAM_EMAIL_REQUIRED);
        }
        
        if (input == null ||
                input.getPassword() == null ||
                input.getPassword().trim().equals("")) {
            throw new BadRequestException(ExceptionMessages.EX_PARAM_PASSWORD_REQUIRED);
        }
        
        if (input == null ||
                input.getDisplayName() == null ||
                input.getDisplayName().trim().equals("")) {
            throw new BadRequestException(ExceptionMessages.EX_PARAM_DISPLAYED_NAME_REQUIRED);
        }
        
        if (!AppUtil.validateEmail(input.getEmail())) {
            throw new BadRequestException(ExceptionMessages.EX_PARAM_EMAIL_INVALID);
        }

        UserDAO dao = DAOFactory.getUserDAO();
        User newUser = dao.findByEmail(input.getEmail());
        
        if (newUser != null && Constants.YES_STATUS.equals(newUser.getStatus())){
            throw new InternalErrorException(ExceptionMessages.EX_EMAIL_EXIST);
		} 

        if (newUser == null) {
        	newUser = new User();
		}
        newUser.setEmail(input.getEmail());
        try {
            byte[] salt = PasswordHelper.generateSalt();
            byte[] encryptedPassword = PasswordHelper.getEncryptedPassword(input.getPassword(), salt);
            newUser.setPassword(ByteBuffer.wrap(encryptedPassword));
            newUser.setSalt(ByteBuffer.wrap(salt));
        } catch (final NoSuchAlgorithmException e) {
            logger.log("No algrithm found for password encryption\n" + e.getMessage());
            throw new InternalErrorException(ExceptionMessages.EX_PWD_SALT);
        } catch (final InvalidKeySpecException e) {
            logger.log("No KeySpec found for password encryption\n" + e.getMessage());
            throw new InternalErrorException(ExceptionMessages.EX_PWD_ENCRYPT);
        }
        newUser.setDisplayName(input.getDisplayName());
        newUser.setType(Constants.USER_TYPE);
        newUser.setActivateCode(RandomUtil.generateActivationKey(Constants.NUM_COUNT));
        newUser.setStatus(Constants.PENDING_STATUS);
        newUser.setImageUrl(input.getImageUrl());
        try {
        	UserIdentity identity;

        	identity = cognito.getUserIdentity(newUser);

            if (identity == null || identity.getIdentityId() == null || identity.getIdentityId().trim().equals("")) {
                logger.log("Could not load Cognito identity ");
                throw new InternalErrorException(ExceptionMessages.EX_NO_COGNITO_IDENTITY);
            }
            newUser.setIdentity(identity);
            
        	newUser = dao.merge(newUser);
        	MailUtil.sendActivateEmail(newUser.getEmail(), newUser.getActivateCode());
        } catch (final DAOException e) {
            logger.log("Error while creating new device\n" + e.getMessage());
            throw new InternalErrorException(ExceptionMessages.EX_DAO_ERROR);
        } catch (CommonException e) {
        	logger.log("Error while creating new device\n" + e.getMessage());
        	//FIXME PhuLTU need to handle transaction here
        	deleteUser(newUser);
            throw new InternalErrorException(ExceptionMessages.EX_SEND_MAIL_ERROR);
		} catch (final AuthorizationException e) {
            logger.log("Error while accessing Cognito\n" + e.getMessage());
            throw new InternalErrorException(ExceptionMessages.EX_NO_COGNITO_IDENTITY);
        }

        if (newUser.getId() == null || newUser.getId().trim().equals("")) {
            logger.log("UserID is null or empty");
            throw new InternalErrorException(ExceptionMessages.EX_DAO_ERROR);
        }

        RegisterUserResponse output = new RegisterUserResponse();
        output.setItem(newUser);
        return getGson().toJson(output);
    }
    
    private void deleteUser(User user) {
    	try {
    		UserDAO dao = DAOFactory.getUserDAO();
			dao.delete(user.getId());
		} catch (DAOException e1) {
			logger.log("Error deleting new device\n" + e1.getMessage());
		}
    }
}

