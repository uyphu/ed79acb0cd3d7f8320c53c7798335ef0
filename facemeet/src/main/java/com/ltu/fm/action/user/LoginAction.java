package com.ltu.fm.action.user;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.JsonObject;
import com.kins.vs.model.device.Device;
import com.kins.vs.model.device.DeviceDAO;
import com.ltu.fm.action.AbstractLambdaAction;
import com.ltu.fm.auth.AWSAuth;
import com.ltu.fm.auth.Token;
import com.ltu.fm.auth.TokenProvider;
import com.ltu.fm.configuration.ExceptionMessages;
import com.ltu.fm.constants.Constants;
import com.ltu.fm.dao.factory.DAOFactory;
import com.ltu.fm.exception.AuthorizationException;
import com.ltu.fm.exception.BadRequestException;
import com.ltu.fm.exception.DAOException;
import com.ltu.fm.exception.InternalErrorException;
import com.ltu.fm.helper.PasswordHelper;
import com.ltu.fm.model.action.user.LoginUserRequest;
import com.ltu.fm.model.action.user.LoginUserResponse;
import com.ltu.fm.model.user.User;
import com.ltu.fm.model.user.UserCredentials;
import com.ltu.fm.model.user.UserDAO;
import com.ltu.fm.model.user.UserIdentity;
import com.ltu.fm.provider.CredentialsProvider;
import com.ltu.fm.provider.ProviderFactory;

/**
 * Action that creates a new User
 * <p/>
 * POST to /pets/
 */
public class LoginAction extends AbstractLambdaAction{
	private LambdaLogger logger;
	
	private CredentialsProvider cognito = ProviderFactory.getCredentialsProvider();

    public String handle(JsonObject request, Context lambdaContext) throws BadRequestException, InternalErrorException {
    	logger = lambdaContext.getLogger();

        LoginUserRequest input = getGson().fromJson(request, LoginUserRequest.class);

        if (input == null ||
                input.getEmail() == null ||
                input.getEmail().trim().equals("") ||
                input.getPassword() == null ||
                input.getPassword().trim().equals("")) {
            throw new BadRequestException(ExceptionMessages.EX_INVALID_INPUT);
        }

        UserDAO dao = DAOFactory.getUserDAO();
        User loggedUser;
        try {
            loggedUser = dao.getUserByEmail(input.getEmail());
        } catch (final DAOException e) {
            logger.log("Error while loading user\n" + e.getMessage());
            throw new InternalErrorException(ExceptionMessages.EX_DAO_ERROR);
        }

        if (loggedUser == null) {
            throw new BadRequestException(ExceptionMessages.EX_EMAIL_NOT_EXIST);
        }
        
        if (!Constants.YES_STATUS.equals(loggedUser.getStatus())) {
            throw new BadRequestException(ExceptionMessages.EX_USER_NOT_ACTIVATED);
        }

        try {
            if (!PasswordHelper.authenticate(input.getPassword(), loggedUser.getPasswordBytes(), loggedUser.getSaltBytes())) {
                throw new BadRequestException(ExceptionMessages.EX_PASSWORD_INCORRECT);
            }
        } catch (final NoSuchAlgorithmException e) {
            logger.log("No algrithm found for password encryption\n" + e.getMessage());
            throw new InternalErrorException(ExceptionMessages.EX_PWD_SALT);
        } catch (final InvalidKeySpecException e) {
            logger.log("No KeySpec found for password encryption\n" + e.getMessage());
            throw new InternalErrorException(ExceptionMessages.EX_PWD_ENCRYPT);
        }
        
        //Update device
        updateDevice(input, loggedUser.getId());

        //Output token
        TokenProvider provider = TokenProvider.getInstance();
    	Token token = provider.createToken(loggedUser);
    	
    	UserIdentity identity;
        UserCredentials credentials;
        try {
            identity = cognito.getUserIdentity(loggedUser);
            loggedUser.setIdentity(identity);
            credentials = cognito.getUserCredentials(loggedUser);
        } catch (final AuthorizationException e) {
            logger.log("Error while getting oidc token through Cognito\n" + e.getMessage());
            throw new InternalErrorException(ExceptionMessages.EX_NO_COGNITO_IDENTITY);
        }
        
        //FIXME need to check cognito
        AWSAuth auth = new AWSAuth(loggedUser.getCognitoIdentityId(), identity.getOpenIdToken(), credentials);
    	
    	LoginUserResponse output = new LoginUserResponse();
    	output.setExpires(token.getExpires());
    	output.setToken(token.getToken());
    	output.setType(token.getType());
    	output.setItem(loggedUser);
    	output.setAuth(auth);
        return getGsonExcludeFields().toJson(output);
    }
    
    private void updateDevice(LoginUserRequest input, String userId) throws InternalErrorException{
    	if (input.getPhoneId() != null) {
    		try {
    			DeviceDAO deviceDAO = DAOFactory.getDeviceDAO();
				Device device = deviceDAO.findByPhoneId(input.getPhoneId());
				if (device != null) {
					device.setLastLoginUserId(userId);
					//device.setPushToken(input.getPushToken());
					deviceDAO.update(device);
				}
			} catch (DAOException e) {
				logger.log("Erro updating device.\n" + e.getMessage());
	            throw new InternalErrorException(ExceptionMessages.EX_UPDATE_DEVICE);
			}
		}
    }
    
}


