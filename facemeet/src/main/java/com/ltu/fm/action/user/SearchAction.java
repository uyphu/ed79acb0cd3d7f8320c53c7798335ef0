package com.ltu.fm.action.user;

import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.JsonObject;
import com.ltu.fm.action.AbstractLambdaAction;
import com.ltu.fm.configuration.ExceptionMessages;
import com.ltu.fm.dao.factory.DAOFactory;
import com.ltu.fm.exception.BadRequestException;
import com.ltu.fm.exception.InternalErrorException;
import com.ltu.fm.model.action.user.SearchUserRequest;
import com.ltu.fm.model.action.user.SearchUserResponse;
import com.ltu.fm.model.user.User;
import com.ltu.fm.model.user.UserDAO;

public class SearchAction extends AbstractLambdaAction{
	//private LambdaLogger logger;

    public String handle(JsonObject request, Context lambdaContext) throws BadRequestException, InternalErrorException {
        //logger = lambdaContext.getLogger();

        SearchUserRequest input = getGson().fromJson(request, SearchUserRequest.class);

        if (input == null ||
                input.getQuery() == null ||
                input.getQuery().trim().equals("")) {
            throw new BadRequestException(ExceptionMessages.EX_INVALID_INPUT);
        }

        UserDAO dao = DAOFactory.getUserDAO();

        List<User> list = dao.search(input.getQuery(), input.getLimit(), input.getCursor());

        SearchUserResponse output = new SearchUserResponse();
        output.setItems(list);

        return getGson().toJson(output);
    }
}