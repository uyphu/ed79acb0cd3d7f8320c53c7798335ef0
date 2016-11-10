/*
 * Copyright 2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package com.ltu.fm.model.user;

import java.util.List;

import com.ltu.fm.exception.DAOException;

// TODO: Auto-generated Javadoc
/**
 * This interface defines the methods required for an implementation of the UserDAO object.
 */
public interface UserDAO {
    
    /**
     * Find a user by its unique username.
     *
     * @param email the email
     * @return A populated User object if the user was found, null otherwise
     * @throws DAOException the DAO exception
     */
    User getUserByEmail(String email) throws DAOException;

    /**
     * Creates a new user in the data store.
     *
     * @param user The new user information
     * @return The username of the user that was created
     * @throws DAOException the DAO exception
     */
    String createUser(User user) throws DAOException;
    
    
    /**
     * Insert.
     *
     * @param user the user
     * @return the device
     * @throws DAOException the DAO exception
     */
    User insert(User user) throws DAOException;
    
    /**
     * Update.
     *
     * @param user the user
     * @return the device
     * @throws DAOException the DAO exception
     */
    User update(User user) throws DAOException;
    
    /**
     * Merge.
     *
     * @param user the user
     * @return the device
     * @throws DAOException the DAO exception
     */
    User merge(User user) throws DAOException;
    
    /**
     * Delete.
     *
     * @param id the id
     * @throws DAOException the DAO exception
     */
    void delete(String id) throws DAOException;
    
    /**
     * Gets the.
     *
     * @param id the id
     * @return the device
     */
    User find(String id);
    
    /**
     * Find by activate code.
     *
     * @param activateCode the activate code
     * @return the user
     * @throws DAOException the DAO exception
     */
    User findByActivateCode(String activateCode) throws DAOException;
    
    /**
     * Activate user.
     *
     * @param activateCode the activate code
     * @return the user
     * @throws DAOException the DAO exception
     */
    User activateUser(String activateCode) throws DAOException;
    
    /**
     * Find by email.
     *
     * @param email the email
     * @return the user
     */
    User findByEmail(String email);
    
    /**
     * Search.
     *
     * @param query the query
     * @param limit the limit
     * @param cursor the cursor
     * @return the list
     */
    List<User> search(String query, int limit, String cursor);
    
    /**
     * Mapper scan.
     *
     * @param query the query
     * @param limit the limit
     * @param cursor the cursor
     * @return the list
     */
    public List<User> mapperScan(String query, int limit, String cursor);
    
    /**
     * Scan.
     *
     * @param query the query
     * @param limit the limit
     * @param cursor the cursor
     * @return the list
     */
    public List<User> scan(String query, int limit, String cursor);
    
}
