package com.ltu.fm.model.device;

import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.ltu.fm.exception.DAOException;

/**
 * The Interface DeviceDAO.
 */
public interface DeviceDAO {
    
    /**
     * Insert.
     *
     * @param user the user
     * @return the device
     * @throws DAOException the DAO exception
     */
    Device insert(Device user) throws DAOException;
    
    /**
     * Update.
     *
     * @param user the user
     * @return the device
     * @throws DAOException the DAO exception
     */
    Device update(Device user) throws DAOException;
    
    /**
     * Merge.
     *
     * @param user the user
     * @return the device
     * @throws DAOException the DAO exception
     */
    Device merge(Device user) throws DAOException;
    
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
     * @throws DAOException the DAO exception
     */
    Device find(String id);
    
    /**
     * Search.
     *
     * @param query the query
     * @param limit the limit
     * @param cursor the cursor
     * @return the list
     * @throws DAOException the DAO exception
     */
    List<Device> search(String query, Integer limit, String cursor);
    
    /**
     * Find by phone id.
     *
     * @param phoneId the phone id
     * @return the device
     */
    Device findByPhoneId(String phoneId);
    
    /**
     * Scan.
     *
     * @param query the query
     * @param limit the limit
     * @param cursor the cursor
     * @return the list
     */
    List<Device> scan(String query, Integer limit, String cursor);
    
    /**
     * Query.
     *
     * @param query the query
     * @param limit the limit
     * @param cursor the cursor
     * @return the list
     */
    List<Device> mapperScan(String query, int limit, String cursor);
    
    /**
     * Mapper query.
     *
     * @param query the query
     * @param limit the limit
     * @param cursor the cursor
     * @return the list
     */
    List<Device> mapperQuery(String query, int limit, String cursor);
    
    /**
     * Find by user id.
     *
     * @param userId the user id
     * @param limit the limit
     * @param cursor the cursor
     * @return the query result page
     */
    QueryResultPage<Device> findByUserId(String userId, Integer limit, String cursor);
    
    /**
     * Find by phone id.
     *
     * @param phoneId the phone id
     * @param limit the limit
     * @param cursor the cursor
     * @return the query result page
     */
    QueryResultPage<Device> findByPhoneId(String phoneId, Integer limit, String cursor);
    
}
