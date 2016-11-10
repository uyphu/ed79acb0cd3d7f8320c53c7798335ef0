package com.ltu.fm.dao;


/**
 * The Interface Dao.
 *
 * @param <T> the generic type
 */
public interface Dao<T> {
	

	/**
	 * Find.
	 *
	 * @param id the id
	 * @return the t
	 */
	T find(String id);
	
	/**
	 * Gets the.
	 *
	 * @param id the id
	 * @return the t
	 */
	T get(String id);

	/**
	 * Insert.
	 *
	 * @param t the t
	 * @return the t
	 */
	T save(T t);
	
	/**
	 * Delete.
	 *
	 * @param t the t
	 */
	void delete(T t);
	

}
