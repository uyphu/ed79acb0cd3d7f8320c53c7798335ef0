package com.ltu.vs.cron;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.ltu.fm.configuration.DynamoDBConfiguration;
import com.ltu.fm.constants.Constants;
import com.ltu.fm.dao.factory.DAOFactory;
import com.ltu.fm.exception.DAOException;
import com.ltu.fm.model.geo.UserPoint;
import com.ltu.fm.model.geo.UserPointDAO;

public class ScheduleHandler implements RequestHandler<String, String> {

	/** The logger. */
	private LambdaLogger logger;

	@Override
	public String handleRequest(String input, Context lambdaContext) {
		logger = lambdaContext.getLogger();
		deleteConferenceAll();
		return null;
	}
	
	private void deleteConferenceAll() {
		UserPointDAO userPointDAO = DAOFactory.getUserPointDAO();

		//Date current = AppUtil.getUTCDateTime();
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		calendar.add(Calendar.MINUTE, 5);
		Date current = calendar.getTime();
        SimpleDateFormat dateFormatter = new SimpleDateFormat(Constants.LONG_DATE_FORMAT);
        dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                
        QueryResultPage<UserPoint> list;
		String cursor = null;
		do {
			list = userPointDAO.findByCreatedAt(dateFormatter.format(current), DynamoDBConfiguration.SCAN_LIMIT, cursor);

			if (list == null || list.getResults().isEmpty()) {
				return;
			}

			cursor = list.getResults().get(list.getResults().size() - 1).getUserId();
			for (UserPoint item : list.getResults()) {
				try {
					userPointDAO.deletePoint(item);
				} catch (DAOException e) {
					logger.log("Error deleting user point" + e.getMessage());
				}
			}

		} while (!list.getResults().isEmpty());

	}
	
}
