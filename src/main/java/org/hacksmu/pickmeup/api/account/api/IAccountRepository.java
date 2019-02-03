package org.hacksmu.pickmeup.api.account.api;

/**
 * An interface for user account storage
 */
public interface IAccountRepository
{
	/**
	 * Creates and stores a new user account
	 * @param email The email address of the new user account
	 * @param passwordHash The hashed password of the new user account
	 * @return int The id of the created account
	 */
	int createAccount(String email, String passwordHash);
	
	/**
	 * Fetches the account data for a user account
	 * @param id The id of the user account
	 * @return {@link IUserAccount} The data of the requested user account
	 */
	IUserAccount selectAccount(int id);
	
	/**
	 * Fetches the account data for a user account
	 * @param email The email address of the user account
	 * @return {@link IUserAccount} The data of the requested user account, or <b>null</b> if no such account exists
	 */
	IUserAccount selectAccount(String email);
	
	/**
	 * Updates the email address for a given user account
	 * @param id The id of the user account
	 * @param newEmail The new email address of the user account
	 */
	void updateAccountEmail(int id, String newEmail);
	
	/**
	 * Updates the password for a given user account
	 * @param id The id of the user account
	 * @param newPassword The new password of the user account
	 */
	void updateAccountPassword(int id, String newPassword);
}