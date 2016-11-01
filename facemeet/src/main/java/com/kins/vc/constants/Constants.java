/*
 * 
 */
package com.kins.vc.constants;

import java.util.Arrays;
import java.util.List;

/**
 * The Class Constants.
 * @author uyphu
 */
public class Constants {
	
	/** The app version. */
	public static String APP_VERSION = "1.0";
	
	/** The get operation. */
	public static String GET_OPERATION = "get";
	
	/** The insert operation. */
	public static String INSERT_OPERATION = "insert";
	
	/** The update operation. */
	public static String UPDATE_OPERATION = "update";
	
	/** The merge operation. */
	public static String MERGE_OPERATION = "merge";
	
	/** The delete operation. */
	public static String DELETE_OPERATION = "delete";
	
	/** The pending status. */
	public static String PENDING_STATUS = "P";
	
	/** The yes status. */
	public static String YES_STATUS = "Y";
	
	/** The no status. */
	public static String NO_STATUS = "N";
	
    /** The Constant SECRET_KEY. */
    public static final String SECRET_KEY = "myXAuthSecret";
    
    /** The Constant AES_SECRET_KEY. */
    public static final String AES_SECRET_KEY = "adfe2342rewdgde1"; // 128 bit key
    
    /** The Constant TOKEN_VALIDITY_IN_SECONDS. */
    public static final int TOKEN_VALIDITY_IN_SECONDS = 7776000; //3 months
    
    /** The user type. */
    public static String USER_TYPE = "U";
    
    /** The facebook type. */
    public static String FACEBOOK_TYPE = "F";
    
    /** The facebook email. */
    public static String FACEBOOK_EMAIL = "@facebook.com";
    
    /** The google email. */
    public static String GOOGLE_EMAIL = "@google.com";
    
    /** The google type. */
    public static String GOOGLE_TYPE = "G";
    
    /** The admin type. */
    public static String ADMIN_TYPE = "A";
    
    /** The wechat type. */
    public static String WECHAT_TYPE = "W";
    
    /** The empty string. */
    public static String EMPTY_STRING = "";
    
    /** The colon string. */
    public static String COLON_STRING = ":";
    
    /** The success. */
    public static String SUCCESS = "success";
    
    /** The max record. */
    public static int MAX_RECORD = 10;
    
    /** The num count. */
    public static int NUM_COUNT = 6;
    
    /** The long date format. */
    public static String LONG_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    
    /** The LON g_ dat e_ forma t1. */
    public static String LONG_DATE_FORMAT1 = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    
    /** The no result json. */
    public static String NO_RESULT_JSON = "{}";
    
    /** The and string. */
    public static String AND_STRING = "&";
    
    /** The host permission. */
    public static String HOST_PERMISSION = "Host";
    
    /** The guest permission. */
    public static String GUEST_PERMISSION = "Guest";
    
    /** The device type. */
    public static String DEVICE_TYPE = "D";
    
    /** The scene type. */
    public static String SCENE_TYPE = "S";
    
    /** The dash string. */
    public static String DASH_STRING = "-";
    
    /** The access key id. */
    public static String ACCESS_KEY_ID = "accessKey";
    
    /** The secret access key. */
    public static String SECRET_ACCESS_KEY = "secretAccess";
    
    /** The ios type. */
    public static String IOS_TYPE = "I";
    
    /** The android type. */
    public static String ANDROID_TYPE = "A";
    
    /** The ios type. */
    public static String IOS = "IOS";
    
    /** The android type. */
    public static String ANDROID = "Android";
    
    /** The web type. */
    public static String WEB_TYPE = "W";
    
    /** The pm code length. */
    public static int PM_CODE_LENGTH = 11;
    
    /** The password string. */
    public static String PASSWORD_STRING = "passwordvc";
    
    /** The device types. */
    public static List<String> DEVICE_TYPES = Arrays.asList(Constants.ANDROID_TYPE, Constants.IOS_TYPE);
    
    /** The call string. */
    public static String CALL_STRING = " calling you";
    
    /** The invite string. */
    public static String INVITE_STRING = " inviting you";
    
    /** The decline string. */
    public static String DECLINE_STRING = " declining you";
    
    /** The hold string. */
    public static String HOLD_STRING = "The call was hold on";
    
    /** The max user event num. */
    public static int MAX_USER_EVENT_NUM = 6;
    
    /** The hash string. */
    public static String HASH_STRING = "#";
    
    /** The comma string. */
    public static String COMMA_STRING = ",";
    
    /** The semicolon string. */
    public static String SEMICOLON_STRING = ";";
    
    /** The open array string. */
    public static String OPEN_ARRAY_STRING = "[";
    
    /** The close array string. */
    public static String CLOSE_ARRAY_STRING = "]";
    
    /** The app name. */
    public static String APP_NAME = "Smart";
    
    /** The page num limit. */
    public static int PAGE_NUM_LIMIT = 20;
    
    /** The tilde string. */
    public static String TILDE_STRING = "~";
    
    /** The max minute. */
    public static long MAX_MINUTE = 20;
    
    /** The meeting topic string. */
    public static String MEETING_TOPIC_STRING = "'s Meeting";
    
    /** The join limit. */
    public static int JOIN_LIMIT = MAX_USER_EVENT_NUM;
    
    public static String SPACE_STRING = " ";
    
    public static String UTC_STRING = "UTC";
    
    /**
     * The Enum DeviceType.
     */
    public enum DeviceType {
        
        /** The android. */
        ANDROID("A"),
        
        /** The ios. */
        IOS("I");

        /** The code. */
        private String code;

        /**
         * Instantiates a new device type.
         *
         * @param code the code
         */
        DeviceType(String code) {
            this.code = code;
        }

        /**
         * Code.
         *
         * @return the string
         */
        public String code() {
            return code;
        }
        
    }
    
}