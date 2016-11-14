package com.kins.vs.action.device;

import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.JsonObject;
import com.kins.vs.model.action.device.SearchDeviceRequest;
import com.kins.vs.model.action.device.SearchDeviceResponse;
import com.kins.vs.model.device.Device;
import com.kins.vs.model.device.DeviceDAO;
import com.ltu.fm.action.AbstractLambdaAction;
import com.ltu.fm.configuration.ExceptionMessages;
import com.ltu.fm.constants.Constants;
import com.ltu.fm.dao.factory.DAOFactory;
import com.ltu.fm.exception.BadRequestException;
import com.ltu.fm.exception.InternalErrorException;

public class SearchAction extends AbstractLambdaAction {
	//private LambdaLogger logger;

	public String handle(JsonObject request, Context lambdaContext) throws BadRequestException, InternalErrorException {
        //logger = lambdaContext.getLogger();

        SearchDeviceRequest input = getGson().fromJson(request, SearchDeviceRequest.class);

        if (input == null) {
            throw new BadRequestException(ExceptionMessages.EX_INVALID_INPUT);
        }
        
        if (input.getQuery() == null) {
            throw new BadRequestException(ExceptionMessages.EX_INVALID_INPUT);
        }

        QueryResultPage<Device> list= query(input.getQuery(), input.getLimit(), input.getCursor());

        SearchDeviceResponse output = new SearchDeviceResponse();
        output.setItems(list);

        return getGson().toJson(output);
    }
	
	private QueryResultPage<Device> query(String query, Integer limit, String cursor) {

		if (query != null) {
			String[] fields = query.split(Constants.AND_STRING);
			if (fields.length > 1) {
				return null;
			}
			DeviceDAO dao = DAOFactory.getDeviceDAO();
			if (query.indexOf("phoneId:") != -1) {
				String[] array = query.split(":");
				return dao.findByPhoneId(array[1], limit, cursor);
			} else if (query.indexOf("lastLoginUserId:") != -1) {
				String[] array = query.split(":");
				return dao.findByUserId(array[1], limit, cursor);
			} else {
				return null;
			}
		}
		return null;
	}
}