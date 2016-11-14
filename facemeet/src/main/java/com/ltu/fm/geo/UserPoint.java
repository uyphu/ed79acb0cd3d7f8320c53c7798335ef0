package com.ltu.fm.geo;

// TODO: Auto-generated Javadoc
/**
 * The Class UserPoint.
 */
public class UserPoint {
	
	/** The user id. */
	private String userId;
	
	/** The display name. */
	private String displayName;
	
	/** The lat. */
	private double lat;
	
	/** The lng. */
	private double lng;

	/**
	 * Gets the user id.
	 *
	 * @return the user id
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Sets the user id.
	 *
	 * @param userId the new user id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Gets the display name.
	 *
	 * @return the display name
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Sets the display name.
	 *
	 * @param displayName the new display name
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Gets the lat.
	 *
	 * @return the lat
	 */
	public double getLat() {
		return lat;
	}

	/**
	 * Sets the lat.
	 *
	 * @param lat the new lat
	 */
	public void setLat(double lat) {
		this.lat = lat;
	}

	/**
	 * Gets the lng.
	 *
	 * @return the lng
	 */
	public double getLng() {
		return lng;
	}

	/**
	 * Sets the lng.
	 *
	 * @param lng the new lng
	 */
	public void setLng(double lng) {
		this.lng = lng;
	}

	public UserPoint(String userId, String displayName, double lat, double lng) {
		this.userId = userId;
		this.displayName = displayName;
		this.lat = lat;
		this.lng = lng;
	}
	
	public UserPoint() {
		
	}
}
