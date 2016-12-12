package com.ltu.fm.action.device;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.JsonObject;
import com.ltu.fm.action.AbstractLambdaAction;
import com.ltu.fm.configuration.ExceptionMessages;
import com.ltu.fm.dao.factory.DAOFactory;
import com.ltu.fm.exception.BadRequestException;
import com.ltu.fm.exception.InternalErrorException;
import com.ltu.fm.model.action.device.DeviceResponse;
import com.ltu.fm.model.action.device.GetDeviceRequest;
import com.ltu.fm.model.device.Device;
import com.ltu.fm.model.device.DeviceDAO;

public class GetAction extends AbstractLambdaAction {
	//private LambdaLogger logger;

    public String handle(JsonObject request, Context lambdaContext) throws BadRequestException, InternalErrorException {
        //logger = lambdaContext.getLogger();

        GetDeviceRequest input = getGson().fromJson(request, GetDeviceRequest.class);

        if (input == null ||
                input.getId() == null ||
                input.getId().trim().equals("")) {
            throw new BadRequestException(ExceptionMessages.EX_INVALID_INPUT);
        }

        DeviceDAO dao = DAOFactory.getDeviceDAO();

        Device getDevice = dao.find(input.getId());

        DeviceResponse output = new DeviceResponse();
        output.setItem(getDevice);

        return getGson().toJson(output);
    }
}
