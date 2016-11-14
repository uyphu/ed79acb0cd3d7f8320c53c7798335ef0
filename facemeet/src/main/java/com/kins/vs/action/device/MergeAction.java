package com.kins.vs.action.device;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.JsonObject;
import com.kins.vs.model.action.device.DeviceResponse;
import com.kins.vs.model.action.device.MergeDeviceRequest;
import com.kins.vs.model.device.Device;
import com.kins.vs.model.device.DeviceDAO;
import com.ltu.fm.action.AbstractLambdaAction;
import com.ltu.fm.configuration.ExceptionMessages;
import com.ltu.fm.dao.factory.DAOFactory;
import com.ltu.fm.exception.BadRequestException;
import com.ltu.fm.exception.DAOException;
import com.ltu.fm.exception.InternalErrorException;
import com.ltu.fm.utils.AppUtil;

public class MergeAction extends AbstractLambdaAction {
	private LambdaLogger logger;

    public String handle(JsonObject request, Context lambdaContext) throws BadRequestException, InternalErrorException {
        logger = lambdaContext.getLogger();

        MergeDeviceRequest input = getGson().fromJson(request, MergeDeviceRequest.class);

        validateInput(input);

        Device mergeDevice = merge(input);

        if (mergeDevice.getId() == null || mergeDevice.getId().trim().equals("")) {
            logger.log("DeviceID is null or empty");
            throw new InternalErrorException(ExceptionMessages.EX_DAO_ERROR);
        }

        DeviceResponse output = new DeviceResponse();
        output.setItem(mergeDevice);

        //FIXME need to check why getGsonExcludeFields() could not apply here
        return getGson().toJson(output);
    }

	private Device merge(MergeDeviceRequest input) throws BadRequestException, InternalErrorException {
		DeviceDAO dao = DAOFactory.getDeviceDAO();

//        Device mergeDevice = new Device();
//        if (input.getId() != null && !input.getId().trim().equals("")) {
//        	mergeDevice.setId(input.getId());
//        	mergeDevice = dao.find(input.getId());
//        	if (mergeDevice == null ) {
//        		throw new BadRequestException(ExceptionMessages.EX_DEVICE_NOT_FOUND);
//			}
//		} else {
//			mergeDevice = dao.findByPhoneId(input.getPhoneId());
//			if (mergeDevice == null) {
//				mergeDevice = new Device();
//				mergeDevice.setId(null);
//			}
//		}
		Device mergeDevice = dao.find(input.getPhoneId());
		if (mergeDevice == null) {
			mergeDevice = new Device();
			mergeDevice.setId(input.getPhoneId());
		}
        mergeDevice.setPhoneId(input.getPhoneId());
        mergeDevice.setType(input.getType());
        mergeDevice.setPushToken(input.getPushToken());
        
        if (input.getLastLoginUserId() != null && !mergeDevice.getLastLoginUserId().trim().equals("")) {
        	mergeDevice.setLastLoginUserId(input.getLastLoginUserId());
		}

        try {
        	mergeDevice = dao.merge(mergeDevice);
        } catch (final DAOException e) {
            logger.log("Error while creating new device\n" + e.getMessage());
            throw new InternalErrorException(ExceptionMessages.EX_DAO_ERROR);
        }
		return mergeDevice;
	}

	private void validateInput(MergeDeviceRequest input) throws BadRequestException {
		if (input == null) {
            throw new BadRequestException(ExceptionMessages.EX_INVALID_INPUT);
        }
        
        if (input.getPhoneId() == null ||
                input.getPhoneId().trim().equals("")) {
            throw new BadRequestException(ExceptionMessages.EX_PARAM_PHONE_ID_REQUIRED);
        }
        
        //Fix login 2016-09-01
//        if (input.getLastLoginUserId() == null ||
//                input.getLastLoginUserId().trim().equals("")) {
//            throw new BadRequestException(ExceptionMessages.EX_PARAM_LAST_LOGIN_ID_REQUIRED);
//        }
        
        if (input.getPushToken() == null ||
                input.getPushToken().trim().equals("")) {
            throw new BadRequestException(ExceptionMessages.EX_PARAM_TOKEN_REQUIRED);
        }
        
        if (input.getType() == null ||
                input.getType().trim().equals("")) {
            throw new BadRequestException(ExceptionMessages.EX_INVALID_INPUT);
        }
        
        if (!AppUtil.hasDeviceType(input.getType())) {
            throw new BadRequestException(ExceptionMessages.EX_DEVICE_TYPE_INVALID);
        }
	}

}
