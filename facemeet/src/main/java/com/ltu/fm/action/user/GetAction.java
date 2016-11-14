package com.ltu.fm.action.user;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.JsonObject;
import com.ltu.fm.action.AbstractLambdaAction;
import com.ltu.fm.configuration.ExceptionMessages;
import com.ltu.fm.dao.factory.DAOFactory;
import com.ltu.fm.exception.BadRequestException;
import com.ltu.fm.exception.InternalErrorException;
import com.ltu.fm.model.action.user.GetUserRequest;
import com.ltu.fm.model.action.user.UserResponse;
import com.ltu.fm.model.user.User;
import com.ltu.fm.model.user.UserDAO;

public class GetAction extends AbstractLambdaAction {
	//private LambdaLogger logger;

    public String handle(JsonObject request, Context lambdaContext) throws BadRequestException, InternalErrorException {
        //logger = lambdaContext.getLogger();

        GetUserRequest input = getGson().fromJson(request, GetUserRequest.class);

        if (input == null ||
                input.getId() == null ||
                input.getId().trim().equals("")) {
            throw new BadRequestException(ExceptionMessages.EX_INVALID_INPUT);
        }

        UserDAO dao = DAOFactory.getUserDAO();

        User getUser = dao.find(input.getId());

        if (getUser == null) {
            throw new InternalErrorException(ExceptionMessages.EX_USER_NOT_FOUND);
		}

        UserResponse output = new UserResponse();
        output.setItem(getUser);

        return getGson().toJson(output);
    }
}
