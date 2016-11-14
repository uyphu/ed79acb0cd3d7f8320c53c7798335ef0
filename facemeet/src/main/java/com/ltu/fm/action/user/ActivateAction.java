package com.ltu.fm.action.user;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.JsonObject;
import com.ltu.fm.configuration.ExceptionMessages;
import com.ltu.fm.dao.factory.DAOFactory;
import com.ltu.fm.exception.BadRequestException;
import com.ltu.fm.exception.DAOException;
import com.ltu.fm.exception.InternalErrorException;
import com.ltu.fm.model.action.user.ActivateUserRequest;
import com.ltu.fm.model.action.user.UserResponse;
import com.ltu.fm.model.user.User;
import com.ltu.fm.model.user.UserDAO;
import com.ltu.fm.action.AbstractLambdaAction;

/**
 * Action that creates a new User
 * <p/>
 * POST to /pets/
 */
public class ActivateAction extends AbstractLambdaAction{
	private LambdaLogger logger;

    public String handle(JsonObject request, Context lambdaContext) throws BadRequestException, InternalErrorException {
        logger = lambdaContext.getLogger();

        logger.log("Calling ActivateAction...");
        ActivateUserRequest input = getGson().fromJson(request, ActivateUserRequest.class);

        if (input == null ||
                input.getActivateCode() == null ||
                input.getActivateCode().trim().equals("")) {
            throw new BadRequestException(ExceptionMessages.EX_INVALID_INPUT);
        }
        
        UserDAO dao = DAOFactory.getUserDAO();
        User user = new User();
        
        try {
        	user = dao.activateUser(input.getActivateCode());
        } catch (final DAOException e) {
            logger.log("Error while creating new device\n" + e.getMessage());
            throw new InternalErrorException(ExceptionMessages.EX_DAO_ERROR);
        } 

        if (user.getId() == null || user.getId().trim().equals("")) {
            logger.log("UserID is null or empty");
            throw new InternalErrorException(ExceptionMessages.EX_DAO_ERROR);
        }

        UserResponse output = new UserResponse();
        output.setItem(user);

        return getGson().toJson(output);
    }
    
}
