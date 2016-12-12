package com.ltu.fm.model.geo;

import java.util.List;

import com.amazonaws.geo.model.GeoPoint;
import com.amazonaws.geo.model.PutPointResult;
import com.ltu.fm.exception.DAOException;
import com.ltu.fm.model.user.User;

public interface UserPointDAO {
	
	PutPointResult putUser(User user) throws DAOException;
	
	List<UserPoint> queryRadius(GeoPoint centerPoint, double radiusInMeter, Integer limit, String cursor) throws DAOException;

}
