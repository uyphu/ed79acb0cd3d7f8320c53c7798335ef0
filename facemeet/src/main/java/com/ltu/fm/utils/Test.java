/**
 * 
 */
package com.ltu.fm.utils;

import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.TreeMap;

import org.json.JSONObject;

import pl.zientarski.SchemaMapper;

import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.ltu.fm.model.action.user.ActivateUserRequest;
import com.ltu.fm.model.action.user.PutUserPointRequest;
import com.ltu.fm.model.action.user.QueryRadiusRequest;
import com.ltu.fm.model.action.user.QueryUserPointResponse;
import com.ltu.fm.model.action.user.RegisterUserRequest;
import com.ltu.fm.model.action.user.RegisterUserResponse;
import com.ltu.fm.model.action.user.UserResponse;


/**
 * @author PhuLTU
 *
 */
public class Test {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Json schema...");
		
		JSONObject schema = new SchemaMapper().toJsonSchema4(QueryUserPointResponse.class, true);
		System.out.println(schema.toString());
		
		System.out.println(Calendar.getInstance().getTime().getTime());
		
		DateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		
	}

}

