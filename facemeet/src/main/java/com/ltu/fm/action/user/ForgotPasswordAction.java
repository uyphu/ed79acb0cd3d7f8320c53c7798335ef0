package com.ltu.fm.action.user;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.JsonObject;
import com.ltu.fm.action.AbstractLambdaAction;
import com.ltu.fm.configuration.AppConfiguration;
import com.ltu.fm.configuration.ExceptionMessages;
import com.ltu.fm.constants.Constants;
import com.ltu.fm.dao.factory.DAOFactory;
import com.ltu.fm.exception.BadRequestException;
import com.ltu.fm.exception.CommonException;
import com.ltu.fm.exception.DAOException;
import com.ltu.fm.exception.InternalErrorException;
import com.ltu.fm.model.action.user.FogotPasswordRequest;
import com.ltu.fm.model.action.user.UserResponse;
import com.ltu.fm.model.user.User;
import com.ltu.fm.model.user.UserDAO;
import com.ltu.fm.utils.MailUtil;
import com.ltu.fm.utils.RandomUtil;
import com.ltu.fm.utils.S3ResourceLoaderUtil;

/**
 * Action that creates a new User
 * <p/>
 * POST to /pets/
 */
public class ForgotPasswordAction extends AbstractLambdaAction{
	
	/** The logger. */
	private LambdaLogger logger;

    public String handle(JsonObject request, Context lambdaContext) throws BadRequestException, InternalErrorException {
        logger = lambdaContext.getLogger();
        
        FogotPasswordRequest input = getGson().fromJson(request, FogotPasswordRequest.class);
        User user = validateInput(input);
        user = callAPI(user);

        return output(user);
    }

	private String output(User user) {
		UserResponse output = new UserResponse();
        output.setItem(user);
        return getGson().toJson(output);
	}

	private User callAPI(User user) throws InternalErrorException {
		UserDAO dao = DAOFactory.getUserDAO();
		String changePasswordKey = RandomUtil.generateActivationKey(Constants.NUM_COUNT);
    	user.setActivateCode(changePasswordKey);
    	try {
    		String message = buildMessage(user.getEmail(), changePasswordKey);  
    		MailUtil.sendEmail(user.getEmail(), "[Video Conference App] Change password key", message);
    		user = dao.update(user);
		} catch (CommonException ce) {
			logger.log("Error sending email " + ce.getMessage());
			throw new InternalErrorException(ExceptionMessages.EX_SEND_MAIL_ERROR);
		} catch (DAOException ex) {
			logger.log("Error update user " + ex.getMessage());
			throw new InternalErrorException(ExceptionMessages.EX_UPDATE_DEVICE);
		}
		return user;
	}
	
	public static String buildMessage(String email, String changePasswordKey) {
		StringBuilder builder = new StringBuilder();
		builder.append("Hi, \nIn order to change password, please go to this link: ");
		builder.append(S3ResourceLoaderUtil.getProperty(AppConfiguration.WEB_URL_KEY));
		builder.append("changepassword/?email="+email+"&changekey="+changePasswordKey);
		return builder.toString();
	}

	private User validateInput(FogotPasswordRequest input) throws BadRequestException {
		if (input == null ||
                input.getEmail() == null ||
                input.getEmail().trim().equals("")) {
            throw new BadRequestException(ExceptionMessages.EX_INVALID_INPUT);
        }
		
		UserDAO dao = DAOFactory.getUserDAO();
        User user = dao.findByEmail(input.getEmail());
        
        if (user == null || !Constants.YES_STATUS.equals(user.getStatus())){
            throw new BadRequestException(ExceptionMessages.EX_PARAM_EMAIL_NOT_EXIST_OR_ACTIVATED);
		} 
        
        if (!Constants.USER_TYPE.equals(user.getType())){
            throw new BadRequestException(ExceptionMessages.EX_PARAM_LOGIN_WITH_FACEBOOK_OR_GOOGLE);
		} 
        
        return user;
	}
    
	public static void main(String[] args) {
		System.out.println(buildMessage("uyphu@yahoo.com", "34342"));
	}

}

