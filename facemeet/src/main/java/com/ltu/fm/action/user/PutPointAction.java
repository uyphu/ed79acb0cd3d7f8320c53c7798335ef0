package com.ltu.fm.action.user;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.JsonObject;
import com.ltu.fm.action.AbstractLambdaAction;
import com.ltu.fm.configuration.ExceptionMessages;
import com.ltu.fm.dao.factory.DAOFactory;
import com.ltu.fm.exception.BadRequestException;
import com.ltu.fm.exception.DAOException;
import com.ltu.fm.exception.InternalErrorException;
import com.ltu.fm.geo.UserPointDAO;
import com.ltu.fm.model.action.user.PutUserPointRequest;
import com.ltu.fm.model.action.user.UserResponse;
import com.ltu.fm.model.user.User;
import com.ltu.fm.model.user.UserDAO;

public class PutPointAction extends AbstractLambdaAction{
	private LambdaLogger logger;

    public String handle(JsonObject request, Context lambdaContext) throws BadRequestException, InternalErrorException {
        logger = lambdaContext.getLogger();

        PutUserPointRequest input = getGson().fromJson(request, PutUserPointRequest.class);

        if (input == null ||
                input.getUserId() == null ||
                input.getUserId().trim().equals("")) {
            throw new BadRequestException(ExceptionMessages.EX_INVALID_INPUT);
        }
        
        UserPointDAO dao = DAOFactory.getUserPointDAO();
        UserDAO userDAO = DAOFactory.getUserDAO();
        User updateUser;
		

        try {
        	updateUser = userDAO.find(input.getUserId());
        	if (updateUser != null) {
        		updateUser.setLat(input.getLat());
        		updateUser.setLng(input.getLng());
        		dao.putUser(updateUser);
        		userDAO.update(updateUser);
			}
        } catch (final DAOException e) {
            logger.log("Error while creating new device\n" + e.getMessage());
            throw new InternalErrorException(ExceptionMessages.EX_DAO_ERROR);
        }

        if (updateUser.getId() == null || updateUser.getId().trim().equals("")) {
            logger.log("UserID is null or empty");
            throw new InternalErrorException(ExceptionMessages.EX_DAO_ERROR);
        }

        UserResponse output = new UserResponse();
        output.setItem(updateUser);

        return getGson().toJson(output);
    }

}