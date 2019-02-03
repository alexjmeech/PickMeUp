package org.hacksmu.pickmeup.api.account.api;

import org.hacksmu.pickmeup.api.account.AccessLevel;

/**
 * Interface for user accounts
 */
public interface IUserAccount
{
	/**
	 * Getter method for this user's internal ID
	 * @return int The internal ID of this user
	 */
	int getID();
	
	/**
	 * Getter method for this user's registered email address
	 * @return String The email address registered to this user
	 */
	String getEmail();
	
	/**
	 * Getter method for this user's hashed password
	 * @return String The hashed password for this user
	 */
	String getPasswordHash();
	
	AccessLevel getAccessLevel();
	
	/**
	 * Setter method for this user's registered email address
	 * @param newPassword The new email address being set for this user
	 */
	void setEmail(String newEmail);
	
	/**
	 * Setter method for this user's hashed password
	 * @param newPassword The new hashed password being set for this user
	 */
	void setPasswordHash(String newPassword);
}