package com.ltu.fm.action.device;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.JsonObject;
import com.ltu.fm.action.AbstractLambdaAction;
import com.ltu.fm.configuration.ExceptionMessages;
import com.ltu.fm.dao.factory.DAOFactory;
import com.ltu.fm.exception.BadRequestException;
import com.ltu.fm.exception.DAOException;
import com.ltu.fm.exception.InternalErrorException;
import com.ltu.fm.model.action.device.DeviceResponse;
import com.ltu.fm.model.action.device.UpdateDeviceRequest;
import com.ltu.fm.model.device.Device;
import com.ltu.fm.model.device.DeviceDAO;
import com.ltu.fm.utils.AppUtil;

public class UpdateAction extends AbstractLambdaAction {
	private LambdaLogger logger;

    public String handle(JsonObject request, Context lambdaContext) throws BadRequestException, InternalErrorException {
        logger = lambdaContext.getLogger();

        UpdateDeviceRequest input = getGson().fromJson(request, UpdateDeviceRequest.class);

        validateInput(input);
        
        Device updateDevice = update(input);

        DeviceResponse output = new DeviceResponse();
        output.setItem(updateDevice);

        return getGson().toJson(output);
    }

	private Device update(UpdateDeviceRequest input) throws BadRequestException, InternalErrorException {
		DeviceDAO dao = DAOFactory.getDeviceDAO();
        Device updateDevice = dao.find(input.getId());
        if (updateDevice == null) {
            throw new BadRequestException(ExceptionMessages.EX_DEVICE_NOT_FOUND);
        }

        try {
        	updateDevice.setPhoneId(input.getPhoneId());
            updateDevice.setType(input.getType());
            updateDevice.setPushToken(input.getPushToken());
            updateDevice.setLastLoginUserId(input.getLastLoginUserId());
        	updateDevice = dao.update(updateDevice);
        } catch (final DAOException e) {
            logger.log("Error while creating new device\n" + e.getMessage());
            throw new InternalErrorException(ExceptionMessages.EX_DAO_ERROR);
        }

        if (updateDevice.getId() == null || updateDevice.getId().trim().equals("")) {
            logger.log("DeviceID is null or empty");
            throw new InternalErrorException(ExceptionMessages.EX_DAO_ERROR);
        }
		return updateDevice;
	}

	private void validateInput(UpdateDeviceRequest input) throws BadRequestException {
		if (input == null ||
                input.getId() == null ||
                input.getId().trim().equals("")) {
            throw new BadRequestException(ExceptionMessages.EX_PARAM_ID_REQUIRED);
        }
        
        if (input.getPhoneId() == null ||
                input.getPhoneId().trim().equals("")) {
            throw new BadRequestException(ExceptionMessages.EX_PARAM_PHONE_ID_REQUIRED);
        }
        
        if (input.getType() == null ||
                input.getType().trim().equals("")) {
            throw new BadRequestException(ExceptionMessages.EX_PARAM_TYPE_REQUIRED);
        }
        
        if (input.getPushToken() == null ||
                input.getPushToken().trim().equals("")) {
            throw new BadRequestException(ExceptionMessages.EX_PARAM_TOKEN_REQUIRED);
        }
        
        if (!AppUtil.hasDeviceType(input.getType())) {
            throw new BadRequestException(ExceptionMessages.EX_DEVICE_TYPE_INVALID);
        }
	}

}