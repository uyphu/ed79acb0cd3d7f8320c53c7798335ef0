package com.ltu.fm.action.user;

import java.util.List;

import com.amazonaws.geo.model.GeoPoint;
import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.JsonObject;
import com.ltu.fm.action.AbstractLambdaAction;
import com.ltu.fm.dao.factory.DAOFactory;
import com.ltu.fm.exception.BadRequestException;
import com.ltu.fm.exception.DAOException;
import com.ltu.fm.exception.InternalErrorException;
import com.ltu.fm.model.action.user.QueryRadiusRequest;
import com.ltu.fm.model.action.user.QueryUserPointResponse;
import com.ltu.fm.model.geo.UserPoint;
import com.ltu.fm.model.geo.UserPointDAO;

public class QueryRadiusAction extends AbstractLambdaAction{
	//private LambdaLogger logger;

    public String handle(JsonObject request, Context lambdaContext) throws BadRequestException, InternalErrorException {
        //logger = lambdaContext.getLogger();

        QueryRadiusRequest input = getGson().fromJson(request, QueryRadiusRequest.class);

        UserPointDAO dao = DAOFactory.getUserPointDAO();
        

        GeoPoint centerPoint = new GeoPoint(input.getLat(), input.getLng());
    	List<UserPoint> result;
		try {
			result = dao.queryRadius(centerPoint, input.getRadiusInMeter(), 2, null);
		} catch (DAOException e) {
			throw new InternalErrorException(e.getMessage());
		}

        QueryUserPointResponse output = new QueryUserPointResponse();
        output.setItems(result);

        return getGson().toJson(output);
    }

}