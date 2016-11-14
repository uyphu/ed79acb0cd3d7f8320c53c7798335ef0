package com.kins.vs.action.device;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.JsonObject;
import com.kins.vs.model.action.device.DeviceResponse;
import com.kins.vs.model.action.device.InsertDeviceRequest;
import com.kins.vs.model.device.Device;
import com.kins.vs.model.device.DeviceDAO;
import com.ltu.fm.action.AbstractLambdaAction;
import com.ltu.fm.configuration.ExceptionMessages;
import com.ltu.fm.dao.factory.DAOFactory;
import com.ltu.fm.exception.BadRequestException;
import com.ltu.fm.exception.DAOException;
import com.ltu.fm.exception.InternalErrorException;
import com.ltu.fm.utils.AppUtil;

/**
 * Action that creates a new Device
 * <p/>
 * POST to /pets/
 */
public class InsertAction extends AbstractLambdaAction {
	private LambdaLogger logger;

    public String handle(JsonObject request, Context lambdaContext) throws BadRequestException, InternalErrorException {
        logger = lambdaContext.getLogger();

        InsertDeviceRequest input = getGson().fromJson(request, InsertDeviceRequest.class);

        validateInput(input);
        Device newDevice = insert(input);

        DeviceResponse output = new DeviceResponse();
        output.setItem(newDevice);

        return getGson().toJson(output);
    }

	private Device insert(InsertDeviceRequest input) throws InternalErrorException {
		DeviceDAO dao = DAOFactory.getDeviceDAO();

        Device newDevice = new Device();
        newDevice.setPhoneId(input.getPhoneId());
        newDevice.setType(input.getType());
        newDevice.setPushToken(input.getPushToken());
        newDevice.setLastLoginUserId(input.getLastLoginUserId());

        try {
        	newDevice = dao.insert(newDevice);
        } catch (final DAOException e) {
            logger.log("Error while creating new device\n" + e.getMessage());
            throw new InternalErrorException(ExceptionMessages.EX_DAO_ERROR);
        }

        if (newDevice.getId() == null || newDevice.getId().trim().equals("")) {
            logger.log("DeviceID is null or empty");
            throw new InternalErrorException(ExceptionMessages.EX_DAO_ERROR);
        }
		return newDevice;
	}

	private void validateInput(InsertDeviceRequest input) throws BadRequestException {
		if (input == null ||
                input.getPhoneId() == null ||
                input.getPhoneId().trim().equals("")) {
            throw new BadRequestException(ExceptionMessages.EX_PARAM_PHONE_ID_REQUIRED);
        }
        
        if (input.getType() == null ||
                input.getType().trim().equals("")) {
            throw new BadRequestException(ExceptionMessages.EX_PARAM_TYPE_REQUIRED);
        }
        
        if (!AppUtil.hasDeviceType(input.getType())) {
            throw new BadRequestException(ExceptionMessages.EX_DEVICE_TYPE_INVALID);
        }
	}

}
