package com.ltu.fm.action.user;

import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.JsonObject;
import com.ltu.fm.action.AbstractLambdaAction;
import com.ltu.fm.configuration.ExceptionMessages;
import com.ltu.fm.constants.Constants;
import com.ltu.fm.dao.factory.DAOFactory;
import com.ltu.fm.exception.BadRequestException;
import com.ltu.fm.exception.DAOException;
import com.ltu.fm.exception.InternalErrorException;
import com.ltu.fm.helper.PasswordHelper;
import com.ltu.fm.model.action.user.InsertUserRequest;
import com.ltu.fm.model.action.user.UserResponse;
import com.ltu.fm.model.user.User;
import com.ltu.fm.model.user.UserDAO;
import com.ltu.fm.utils.RandomUtil;

/**
 * Action that creates a new User
 * <p/>
 * POST to /pets/
 */
public class InsertAction extends AbstractLambdaAction{
	private LambdaLogger logger;

    public String handle(JsonObject request, Context lambdaContext) throws BadRequestException, InternalErrorException {
        logger = lambdaContext.getLogger();

        InsertUserRequest input = getGson().fromJson(request, InsertUserRequest.class);

        if (input == null ||
                input.getEmail() == null ||
                input.getEmail().trim().equals("")) {
            throw new BadRequestException(ExceptionMessages.EX_INVALID_INPUT);
        }
        
        if (input == null ||
                input.getPassword() == null ||
                input.getPassword().trim().equals("")) {
            throw new BadRequestException(ExceptionMessages.EX_INVALID_INPUT);
        }
        
        if (input == null ||
                input.getDisplayName() == null ||
                input.getDisplayName().trim().equals("")) {
            throw new BadRequestException(ExceptionMessages.EX_INVALID_INPUT);
        }

        UserDAO dao = DAOFactory.getUserDAO();

        User newUser = new User();
        newUser.setEmail(input.getEmail());
        try {
            byte[] salt = PasswordHelper.generateSalt();
            byte[] encryptedPassword = PasswordHelper.getEncryptedPassword(input.getPassword(), salt);
            newUser.setPassword(ByteBuffer.wrap(encryptedPassword));
            newUser.setSalt(ByteBuffer.wrap(salt));
        } catch (final NoSuchAlgorithmException e) {
            logger.log("No algrithm found for password encryption\n" + e.getMessage());
            throw new InternalErrorException(ExceptionMessages.EX_PWD_SALT);
        } catch (final InvalidKeySpecException e) {
            logger.log("No KeySpec found for password encryption\n" + e.getMessage());
            throw new InternalErrorException(ExceptionMessages.EX_PWD_ENCRYPT);
        }
        newUser.setDisplayName(input.getDisplayName());
        newUser.setType(Constants.USER_TYPE);
        newUser.setActivateCode(RandomUtil.generateActivationKey(Constants.NUM_COUNT));
        newUser.setStatus(Constants.PENDING_STATUS);
        newUser.setImageUrl(input.getImageUrl());
        try {
        	newUser = dao.insert(newUser);
        } catch (final DAOException e) {
            logger.log("Error while creating new device\n" + e.getMessage());
            throw new InternalErrorException(ExceptionMessages.EX_DAO_ERROR);
        }

        if (newUser.getId() == null || newUser.getId().trim().equals("")) {
            logger.log("UserID is null or empty");
            throw new InternalErrorException(ExceptionMessages.EX_DAO_ERROR);
        }

        UserResponse output = new UserResponse();
        output.setItem(newUser);

        return getGson().toJson(output);
    }

}
