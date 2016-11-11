package com.ltu.fm.geo;

import com.ltu.fm.exception.DAOException;
import com.ltu.fm.model.user.User;

public interface UserPointDAO {
	
	User putUser(User user) throws DAOException;

}
