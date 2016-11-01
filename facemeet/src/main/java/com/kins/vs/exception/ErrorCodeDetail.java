package com.kins.vs.exception;

/**
 * The Enum ErrorCodeDetail.
 * @author uyphu
 */
public enum ErrorCodeDetail {

	/** The error duplicated name. */
	ERROR_DUPLICATED_NAME(600, "Name already exists"), 
	
	/** The error duplicated email. */
	ERROR_DUPLICATED_EMAIL(601, "Email already exists"), 
	
	/** The error exist object. */
	ERROR_EXIST_OBJECT(602, "Object already exists"),
	
	/** The error record not found. */
	ERROR_RECORD_NOT_FOUND(603, "Object not found"),
	
	/** The error parse query. */
	ERROR_PARSE_QUERY(604, "Parsing query string error"),
	
	/** The error remove entity. */
	ERROR_REMOVE_ENTITY(605, "Remove entity error"),
	
	/** The error insert entity. */
	ERROR_INSERT_ENTITY(606, "Insert entity error"),
	
	/** The error update entity. */
	ERROR_UPDATE_ENTITY(607, "Update entity error"),
	
	/** The error conflict login. */
	ERROR_CONFLICT_LOGIN(608, "Login already exists"),
	
	/** The error conflict email. */
	ERROR_CONFLICT_EMAIL(609, "Email already exists"),
	
	/** The error conflict login and email. */
	ERROR_CONFLICT_LOGIN_AND_EMAIL(610, "Login and Email already exists"),
	
	/** The error duplicated role. */
	ERROR_DUPLICATED_ROLE(611, "Duplicated role"),
	
	/** The error not found role. */
	ERROR_NOT_FOUND_ROLE(612, "Role not found"),
	
	/** The error not activated. */
	ERROR_NOT_ACTIVATED(613, "User not activated"),
	
	/** The error invalid password. */
	ERROR_INVALID_PASSWORD(614, "Invalid password"),
	
	/** The error user not found. */
	ERROR_USER_NOT_FOUND(615, "User not found"),
	
	/** The error user not authenticated. */
	ERROR_USER_NOT_AUTHENTICATED(616, "User not authenticated"),
	
	/** The error user not valid. */
	ERROR_USER_NOT_VALID(617, "User not valid"),
	
	/** The error create user table. */
	ERROR_CREATE_USER_TABLE(618, "Error create user table"),
	
	/** The error send email. */
	ERROR_SEND_EMAIL(619, "Error send email"),
	
	/** The error activate user. */
	ERROR_ACTIVATE_USER(620, "Error activate user"),
	
	/** The error input invalid. */
	ERROR_INPUT_INVALID(621, "Error Invalid Input"),
	
	/** The error facebook not authenticated. */
	ERROR_FACEBOOK_NOT_AUTHENTICATED(622, "Error Facebook not authenticated"),
	
	/** The error delete user table. */
	ERROR_DELETE_USER_TABLE(623, "Error delete user table"),
	
	/** The error login user. */
	ERROR_LOGIN_USER(624, "Error login"),
	
	/** The error already activate user. */
	ERROR_ALREADY_ACTIVATE_USER(625, "User already activated"),
	
	/** The error encrypt failure. */
	ERROR_ENCRYPT_FAILURE(626, "Encrypt failure"),
	
	/** The error decrypt failure. */
	ERROR_DECRYPT_FAILURE(627, "Decrypt failure"),
	
	/** The error invalid token. */
	ERROR_INVALID_TOKEN(628, "Invalid token"),
	
	/** The error invalid change password key. */
	ERROR_INVALID_CHANGE_PASSWORD_KEY(629, "Invalid change password key"),
	
	/** The error create tables. */
	ERROR_CREATE_TABLES(630, "Error create tables"),
	
	/** The error home request not found. */
	ERROR_HOME_REQUEST_NOT_FOUND(631, "Home not found"),
	
	/** The error init request not found. */
	ERROR_INIT_REQUEST_NOT_FOUND(632, "Init not found"),
	
	/** The error create device table. */
	ERROR_CREATE_DEVICE_TABLE(633, "Error create device table"),
	
	/** The error delete device table. */
	ERROR_DELETE_DEVICE_TABLE(634, "Error delete device table"),
	
	/** The error device not found. */
	ERROR_DEVICE_NOT_FOUND(635, "Error device not found"),
	
	/** The error duplicated device. */
	ERROR_DUPLICATED_DEVICE(636, "Device already exists"),
	
	/** The ERRO r_ home_ no t_ found. */
	ERROR_HOME_NOT_FOUND(637, "Error Home not found"), 
	
	/** The error device request not found. */
	ERROR_DEVICE_REQUEST_NOT_FOUND(638, "Home not found"),
	
	/** The error operation not found. */
	ERROR_OPERATION_NOT_FOUND(639, "Error operation not found"),
	
	/** The error query string invalid. */
	ERROR_QUERY_STRING_INVALID(640, "Error operation not found"),
	
	/** The error group not found. */
	ERROR_GROUP_NOT_FOUND(641, "Error group not found"),
	
	/** The error cannot delete object in use. */
	ERROR_CANNOT_DELETE_OBJECT_IN_USE(642, "Error cannot delete object in use"),
	
	/** The error facebook id invalid. */
	ERROR_FACEBOOK_ID_INVALID(643, "Error Facebook id invalid"),
	
	/** The error key in use. */
	ERROR_KEY_IN_USE(644, "Error key in use"),
	
	/** The error key invalid. */
	ERROR_KEY_INVALID(645, "Error key invalid"),
	
	/** The error key expired. */
	ERROR_KEY_EXPIRED(646, "Error key expired"),
	
	/** The error key validation. */
	ERROR_KEY_VALIDATION(647, "Error user using access key cannot be the same with user created"),
	
	/** The error clone d. */
	ERROR_CLONE_QUEST_HOME(648, "Error clone guest home"),
	
	/** The error update quest home. */
	ERROR_UPDATE_QUEST_HOME(649, "Error update guest home"),
	
	/** The error quest home invalid. */
	ERROR_QUEST_HOME_INVALID(650, "Error guest home invalid"),
	
	/** The error validate access right. */
	ERROR_VALIDATE_ACCESS_RIGHT(651, "Error validate access right"),
	
	/** The error record sync over limit. */
	ERROR_RECORD_SYNC_OVER_LIMIT(652, "Error records sync over limit"),
	
	/** The error already use another key. */
	ERROR_ALREADY_USE_ANOTHER_KEY(652, "Error user already used another key"),
	
	/** The error token not expired. */
	ERROR_TOKEN_NOT_EXPIRED(653, "Error token not expired"),
	
	/** The error google id invalid. */
	ERROR_GOOGLE_ID_INVALID(655, "Error Google email invalid"),
	
	/** The error google not authenticated. */
	ERROR_GOOGLE_NOT_AUTHENTICATED(656, "Error Google not authenticated");
	
	/** The id. */
	private final int id;
	
	/** The msg. */
	private final String msg;

	/**
	 * Instantiates a new error code.
	 *
	 * @param id the id
	 * @param msg the msg
	 */
	ErrorCodeDetail(int id, String msg) {
		this.id = id;
		this.msg = msg;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Gets the msg.
	 *
	 * @return the msg
	 */
	public String getMsg() {
		return this.msg;
	}
	
	/**
	 * Gets the by id.
	 *
	 * @param id the id
	 * @return the by id
	 */
	public static ErrorCodeDetail getById(int id) {
		for (ErrorCodeDetail e : values()) {
			if (e.getId() == id)
				return e;
		}
		return null;
	}
	
	/**
	 * Gets the msg by id.
	 *
	 * @param id the id
	 * @return the msg by id
	 */
	public static String getMsgById(int id) {
		for (ErrorCodeDetail e : values()) {
			if (e.getId() == id)
				return e.getMsg();
		}
		return null;
	}
	
}
