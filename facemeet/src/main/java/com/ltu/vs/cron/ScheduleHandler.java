package com.ltu.vs.cron;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class ScheduleHandler implements RequestHandler<String, String> {

	/** The logger. */
	private LambdaLogger logger;

	@Override
	public String handleRequest(String input, Context lambdaContext) {
		logger = lambdaContext.getLogger();
		
		return null;
	}

	
}
