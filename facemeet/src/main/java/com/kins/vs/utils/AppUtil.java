/*
 * 
 */
package com.kins.vs.utils;

import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kins.vc.constants.Constants;
import com.kins.vc.constants.Constants.DeviceType;

/**
 * The Class AppUtil.
 * @author PhuLTU
 */
public class AppUtil {
	
	private static final String EMAIL_PATTERN =
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	/** The props. */
	public static Properties props = new Properties();
	
	/**
	 * Gets the current time.
	 *
	 * @return the current time
	 */
	public static String getCurrentTime() {
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS Z");
		return DATE_FORMAT.format(Calendar.getInstance().getTime());
	}
	
	/**
	 * To date.
	 *
	 * @param date the date
	 * @return the date
	 */
	public static Date toDate(String date) throws ParseException {
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(Constants.LONG_DATE_FORMAT);
		return DATE_FORMAT.parse(date);
	}
	
	/**
	 * To date.
	 *
	 * @param date the date
	 * @param format the format
	 * @return the date
	 * @throws ParseException the parse exception
	 */
	public static Date toDate(String date, String format) throws ParseException {
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(format);
		return DATE_FORMAT.parse(date);
	}
	
	/**
	 * To iso date.
	 *
	 * @param date the date
	 * @return the date
	 */
	public static Date toISODate(String date) {
		DateTimeFormatter parser = ISODateTimeFormat.dateTimeParser();
		DateTime dt = parser.parseDateTime(date);
		return dt.toDate();
	}
	
	
	
	/**
	 * To string.
	 *
	 * @param date the date
	 * @return the string
	 */
	public static String toString(Date date) {
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(Constants.LONG_DATE_FORMAT);
		return DATE_FORMAT.format(date);
	}
	
	/**
	 * Gets the cur time.
	 *
	 * @return the cur time
	 */
	public static String getCurTime() {
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(Constants.LONG_DATE_FORMAT);
		return DATE_FORMAT.format(Calendar.getInstance().getTime());
	}
	
	/**
	 * Gets the cur date time.
	 *
	 * @return the cur date time
	 */
	public static Date getCurDateTime() {
		return Calendar.getInstance().getTime();
	}
	
	/**
	 * Gets the UTC date time.
	 *
	 * @return the UTC date time
	 */
	public static Date getUTCDateTime() {
		return Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime();
	}
	
	/**
	 * Send email.
	 *
	 * @param email the email
	 * @param activateKey the activate key
	 * @return true, if successful
	 */
	public static boolean sendEmail(String email, String title,  String activateKey) {
		String FROM = "phule@kinssolutions.net";
		String SUBJECT = title; //"Activate User";
		String BODY = "Hi, This is your activate code: " + activateKey;
		// Construct an object to contain the recipient address.
        Destination destination = new Destination().withToAddresses(new String[]{email});
        
        // Create the subject and body of the message.
        Content subject = new Content().withData(SUBJECT);
        Content textBody = new Content().withData(BODY); 
        Body body = new Body().withText(textBody);
        
        // Create a message with the specified subject and body.
        Message message = new Message().withSubject(subject).withBody(body);
        
        // Assemble the email.
        SendEmailRequest request = new SendEmailRequest().withSource(FROM).withDestination(destination).withMessage(message);
        
        System.out.println("Attempting to send an email through Amazon SES by using the AWS SDK for Java...");
    
        // Instantiate an Amazon SES client, which will make the service call. The service call requires your AWS credentials. 
        // Because we're not providing an argument when instantiating the client, the SDK will attempt to find your AWS credentials 
        // using the default credential provider chain. The first place the chain looks for the credentials is in environment variables 
        // AWS_ACCESS_KEY_ID and AWS_SECRET_KEY. 
        // For more information, see http://docs.aws.amazon.com/AWSSdkDocsJava/latest/DeveloperGuide/credentials.html
        AmazonSimpleEmailServiceClient client = new AmazonSimpleEmailServiceClient();
           
        // Choose the AWS region of the Amazon SES endpoint you want to connect to. Note that your sandbox 
        // status, sending limits, and Amazon SES identity-related settings are specific to a given AWS 
        // region, so be sure to select an AWS region in which you set up Amazon SES. Here, we are using 
        // the US West (Oregon) region. Examples of other regions that Amazon SES supports are US_EAST_1 
        // and EU_WEST_1. For a complete list, see http://docs.aws.amazon.com/ses/latest/DeveloperGuide/regions.html 
        Region REGION = Region.getRegion(Regions.US_WEST_2);
        client.setRegion(REGION);
   
        // Send the email.
        client.sendEmail(request);  
        return true;
	}
	
	/**
	 * To json.
	 *
	 * @param obj the obj
	 * @return the string
	 */
	public static String toJSON(Object obj) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			return null;
		}
	}
	
	/**
	 * To json.
	 *
	 * @param value the value
	 * @return the JSON object
	 */
	public static JSONObject toJSON(String value) throws JSONException {
		return new JSONObject(value);
	}
	
	/**
	 * Crypt with m d5.
	 *
	 * @param password the password
	 * @return the string
	 */
	public static String cryptWithMD5(String password) throws NoSuchAlgorithmException{
		if (password != null) {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] passBytes = password.getBytes();
			md.reset();
			byte[] digested = md.digest(passBytes);
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < digested.length; i++) {
				sb.append(Integer.toHexString(0xff & digested[i]));
			}
			return sb.toString();
		}
		return null;
	}
	
	/**
	 * Encrypt with aes.
	 *
	 * @param value the value
	 * @return the string
	 */
	public static String encryptWithAES(String value) throws Exception {
		Key key = new SecretKeySpec(Constants.AES_SECRET_KEY.getBytes(), "AES");
	    Cipher cipher = Cipher.getInstance("AES");
	    cipher.init(Cipher.ENCRYPT_MODE, key);
	    byte[] encVal = cipher.doFinal(value.getBytes());
	    String encryptedValue = new Base64().encodeToString(encVal);
	    return encryptedValue;
	}

	/**
	 * Decrypt with aes.
	 *
	 * @param encryptedData the encrypted data
	 * @return the string
	 */
	public static String decryptWithAES(String encryptedData) throws Exception {
		Key key = new SecretKeySpec(Constants.AES_SECRET_KEY.getBytes(), "AES");
	    Cipher cipher = Cipher.getInstance("AES");
	    cipher.init(Cipher.DECRYPT_MODE, key);
	    byte[] decordedValue = new Base64().decode(encryptedData);
	    byte[] decValue = cipher.doFinal(decordedValue);
	    String decryptedValue = new String(decValue);
	    return decryptedValue;
	    
	}
	
	/**
	 * Generate unique key.
	 *
	 * @return the string
	 */
	public static String generateUniqueKey() {
		UUID idOne = UUID.randomUUID();
		UUID idTwo = UUID.randomUUID();
		UUID idThree = UUID.randomUUID();
		UUID idFour = UUID.randomUUID();

		String time = idOne.toString().replace("-", "");
		String time2 = idTwo.toString().replace("-", "");
		String time3 = idThree.toString().replace("-", "");
		String time4 = idFour.toString().replace("-", "");

		StringBuffer data = new StringBuffer();
		data.append(time);
		data.append(time2);
		data.append(time3);
		data.append(time4);

		SecureRandom random = new SecureRandom();
		int beginIndex = random.nextInt(100); // Begin index + length of your
												// string < data length
		int endIndex = beginIndex + 10; // Length of string which you want

		return data.substring(beginIndex, endIndex);
	}
	
	/**
	 * Gets the user id.
	 *
	 * @param token the token
	 * @return the user id
	 */
	public static String getUserId(String token) {
		String[] parts = token.split(":");
		return parts[0];
	}
	
	/**
	 * Gets the displayed name.
	 *
	 * @param email the email
	 * @return the displayed name
	 */
	public static String getDisplayedName(String email) {
		String[] parts = email.split("@");
		return parts[0];
	}
	
	/**
	 * Load properties.
	 */
	private static void loadProperties() throws IOException {
		 
		InputStream inputStream;
		String propFileName = "config.properties";
		 
		inputStream = AppUtil.class.getClassLoader().getResourceAsStream(propFileName);

		if (inputStream != null) {
			props.load(inputStream);
		} 

		inputStream.close();
	}
	
	/**
	 * Gets the property.
	 *
	 * @param key the key
	 * @return the property
	 */
	public static String getProperty(String key) throws IOException {
		if (!props.containsKey(key)) {
			loadProperties();
		}
		return props.getProperty(key);
	}
	
	/**
	 * Gets the UTC format.
	 *
	 * @return the UTC format
	 */
	public static DateFormat getUTCFormat() {
		DateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		iso8601Format.setTimeZone(TimeZone.getTimeZone("UTC"));
		return iso8601Format;
    }
	
	public static boolean validateEmail(String email){
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}
	
	public static boolean hasDeviceType(String code) {
    	for (DeviceType string : DeviceType.values()) {
			if(string.code().equals(code)) {
				return true;
			}
		}
    	return false;
    }
	
	/**
	 * Append sort.
	 * Each item with format A~B and list is separated by semicolon ;
	 *
	 * @param list the list
	 * @param item the item
	 * @return the string
	 */
	public static String appendSort(String list, String item) {
		//Each item with format A~B and list is separated by semicolon ;
		if (item != null) {
			if (list != null) {
				list = list + Constants.SEMICOLON_STRING + item;
				String[] array = list.split(Constants.SEMICOLON_STRING);
				
				Arrays.sort(array);
				
				StringBuilder builder = new StringBuilder();
				builder.append(array[0]);
				for (int i = 1; i < array.length; i++) {
					builder.append(Constants.SEMICOLON_STRING);
					builder.append(array[i]);
				}
				return builder.toString();
			} else {
				return item;
			}
		}
		return list;
	}
	
	public static String removeSort(String list, String userId) {
		//Each item with format A~B and list is separated by semicolon ;
		if (userId != null) {
			if (list != null) {
				String[] array = list.split(Constants.SEMICOLON_STRING);
				List<String> strings = new LinkedList<String>(Arrays.asList(array));
				for (String string : strings) {
					if (string.indexOf(userId) != -1) {
						strings.remove(string);
						break;
					}
				}
				StringBuilder builder = new StringBuilder();
				if (strings.size() > 0) {
					
					builder.append(strings.get(0));
					for (int i = 1; i < strings.size(); i++) {
						builder.append(Constants.SEMICOLON_STRING);
						builder.append(strings.get(i));
					}
				}
				
				return builder.toString();
			} 
		}
		return list;
	}
	
	public static long getMinuteWithCurrent(Date date) {
		return (AppUtil.getUTCDateTime().getTime() - date.getTime())/60000;
	}
	
	public static void main(String[] args) {
		String list = "B~1;C~2";
		System.out.println(appendSort(list, "A~1"));
		list = removeSort(list, "A~1");
		System.out.println(list);
		list = removeSort(list, "B~1");
		System.out.println(list);
		list = removeSort(list, "C~2");
		System.out.println(list);
	}
}
