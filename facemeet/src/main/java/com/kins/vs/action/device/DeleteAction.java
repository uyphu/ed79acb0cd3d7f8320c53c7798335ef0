package com.kins.vs.action.device;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.JsonObject;
import com.kins.vs.model.action.device.DeleteDeviceRequest;
import com.kins.vs.model.action.device.DeleteDeviceResponse;
import com.kins.vs.model.device.DeviceDAO;
import com.ltu.fm.action.AbstractLambdaAction;
import com.ltu.fm.configuration.ExceptionMessages;
import com.ltu.fm.dao.factory.DAOFactory;
import com.ltu.fm.exception.BadRequestException;
import com.ltu.fm.exception.DAOException;
import com.ltu.fm.exception.InternalErrorException;

public class DeleteAction extends AbstractLambdaAction {
	private LambdaLogger logger;

    public String handle(JsonObject request, Context lambdaContext) throws BadRequestException, InternalErrorException {
        logger = lambdaContext.getLogger();

        DeleteDeviceRequest input = getGson().fromJson(request, DeleteDeviceRequest.class);

        if (input == null ||
                input.getId() == null ||
                input.getId().trim().equals("")) {
            throw new BadRequestException(ExceptionMessages.EX_INVALID_INPUT);
        }

        DeviceDAO dao = DAOFactory.getDeviceDAO();

        try {
        	dao.delete(input.getId());
        } catch (final DAOException e) {
            logger.log("Error while creating new device\n" + e.getMessage());
            throw new InternalErrorException(ExceptionMessages.EX_DAO_ERROR);
        }

        DeleteDeviceResponse output = new DeleteDeviceResponse();
        output.setItem(null);

        return getGson().toJson(output);
    }
}
