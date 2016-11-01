package com.kins.vs.utils;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

import com.kins.vc.constants.Constants;
import com.kins.vs.configuration.ExceptionMessages;
import com.kins.vs.exception.InternalErrorException;

/**
 * The Class NotificationUtil.
 */
public class NotificationUtil {

	/** The log. */
	private static Logger LOG = Logger.getLogger(NotificationUtil.class);

	/** The android api key. */
	private static String ANDROID_API_KEY = "key=AIzaSyDPahGQaJMgQpeRjUuGYAX2vbi0lwgq4kE";


	/** The ios api key. */
	private static String IOS_API_KEY = "key=AIzaSyDPahGQaJMgQpeRjUuGYAX2vbi0lwgq4kE";

	/**
	 * Push android.
	 *
	 * @param title the title
	 * @param body the body
	 * @param pushToken the push token
	 * @return true, if successful
	 * @throws InternalErrorException the internal error exception
	 */
	public static boolean pushAndroid(String bodyStr) throws InternalErrorException {
		return sendNotification(bodyStr, ANDROID_API_KEY);
	}
	
	/**
	 * Push ios.
	 *
	 * @param title the title
	 * @param body the body
	 * @param pushToken the push token
	 * @return true, if successful
	 * @throws InternalErrorException the internal error exception
	 */
	public static boolean pushIOS(String bodyStr) throws InternalErrorException {
		return sendNotification(bodyStr, IOS_API_KEY);
	}
	
	/**
	 * Push msg.
	 *
	 * @param bodyStr the body str
	 * @param type the type
	 * @return true, if successful
	 * @throws InternalErrorException the internal error exception
	 */
	public static boolean pushMsg(String bodyStr, String type) throws InternalErrorException {
		if (Constants.DeviceType.ANDROID.code().equals(type) || Constants.ANDROID.equals(type)) {
			return sendNotification(bodyStr, ANDROID_API_KEY);
		} else if (Constants.DeviceType.IOS.code().equals(type) || Constants.IOS.equals(type)) {
			return sendNotification(bodyStr, IOS_API_KEY);
		} else {
			//FIXME PhuLTU send to web
			return false;
		}
	}

	/**
	 * Send notification.
	 *
	 * @param title the title
	 * @param body the body
	 * @param pushToken the push token
	 * @param apiKey the api key
	 * @return true, if successful
	 * @throws InternalErrorException the internal error exception
	 */
	private static boolean sendNotification(String bodyStr, String apiKey)
			throws InternalErrorException {
		try {
			URL url = new URL("https://fcm.googleapis.com/fcm/send");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");

			// String apiKey = "AI...wE";
			// String credentials = "key=" + apiKey;

			// String basicAuth = "Basic " +
			// Base64.getEncoder().encodeToString(credentials.getBytes());
			// String basicAuth = "Basic " + new
			// String(Base64.encodeBase64(credentials.getBytes()));

			conn.setRequestProperty("Authorization", apiKey);
			//String bodyStr = buildMessage(title, body, pushToken);

			OutputStream os = conn.getOutputStream();
			os.write(bodyStr.getBytes());
			os.flush();

			if (conn.getResponseCode() != 200) {
				LOG.error("Failed : HTTP error code : " + conn.getResponseCode());
				throw new InternalErrorException(ExceptionMessages.EX_PUSH_NOTIFICATION);
			}

//			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
//
//			String output;
//			System.out.println("Output from Server .... \n");
//			while ((output = br.readLine()) != null) {
//				System.out.println(output);
//			}

			conn.disconnect();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.getCause());
			throw new InternalErrorException(ExceptionMessages.EX_PUSH_NOTIFICATION);
		}

		return true;
	}

	/**
	 * Builds the message.
	 *
	 * @param title the title
	 * @param body the body
	 * @param pushToken the push token
	 * @return the string
	 */
	public static String buildMessage(String title, String body, String pushToken) {
		String notfnStr = "{\"text\": \"" + body + "\", \"title\": \"" + title + "\"}";
		String dataStr = "{\"key1\": \"value1\", \"key2\": \"value2\"}";

		String bodyStr = "{\"priority\": \"high\", \"to\": \"" + pushToken + "\", " + "\"notification\": " + notfnStr
				+ ", \"data\": " + dataStr + "}";
		return bodyStr;
	}
	
	public static void main(String[] args) {
		System.out.println("Pushing notification ...");
		try {
			//pushAndroid(buildMessage("Test lan 2 android", "Test topics ", "ceydKUBw4E4:APA91bFzKUwWVdz_r8baNIcgMBU03wXZWjD9_xZQs-LsdpG18P6cES0eUDlheUXXFf9js89uwCfEiFSwhmRSz7DV7qKyP9YxiVU1hqZbgwMepiEKQGy-I4BUAEZKHb277BILdbsoJUf7"));
			//pushAndroid(buildMessage("Test 2", "Test topics ", "emQTNTgMWd0:APA91bGzYzuwfP9xBBGnILwJIzK6xRTYwRaM0IZce-ysGCHf1EIbxUcFPXvHWK8eDaMNqd0O_rh5hUBY0YsVhgwzCrn-o8jkClnL9QGM5doef0hLJCUoj7qF1Os5tdfM9Lf9JjUAICJO"));
//			pushIOS(buildMessage("Test", "Test topics ", "f5uSIezq7-s:APA91bHwhiU0HgYKNZMpm-UoC5QTkfKxS1bK3QeCwEUCb-dorMG89fUkT-1PilUJXxH10_Efc0XWSDrghx1Ltfa1NwOXg-NizYoKX_qz3wMy493IE7ucLwg9wN6GXNQTybjO5x6F3f0h"));
			pushIOS(buildMessage("Test", "Test topics ", "cndEQvUlops:APA91bG3GrEb_yJLQRy2Ya4QM6a95Dv2HeOCPU_Y663a05aweZtofAXWnjH1JH6mjqZcs7e0sVMATPOGZrdxqxfIoJOTC5r9Ue76jCnSHEgD8j67m-6vhilBbmowi5cnIuBcIvpSnWA9"));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("End ...");
	}

}
