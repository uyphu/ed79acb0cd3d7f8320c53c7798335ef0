package com.ltu.fm.model.geo;

import java.util.List;

import com.amazonaws.geo.model.DeletePointResult;
import com.amazonaws.geo.model.GeoPoint;
import com.amazonaws.geo.model.PutPointResult;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.ltu.fm.exception.DAOException;
import com.ltu.fm.model.user.User;

public interface UserPointDAO {
	
	PutPointResult putUser(User user) throws DAOException;
	
	List<UserPoint> queryRadius(GeoPoint centerPoint, double radiusInMeter, Integer limit, String cursor) throws DAOException;
	
	QueryResultPage<UserPoint> findByCreatedAt(String createdAt, Integer limit, String cursor);
	
	DeletePointResult deletePoint(UserPoint user) throws DAOException;

}
