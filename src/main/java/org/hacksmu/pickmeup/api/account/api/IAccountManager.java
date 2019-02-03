package org.hacksmu.pickmeup.api.account.api;

/**
 * Interface for user account manager
 */
public interface IAccountManager
{
	/**
	 * Creates a {@link IUserSession} after validating user-input email and password
	 * @param email The email address provided by the user
	 * @param password The password provided by the user
	 * @return {@link IUserSession} The session attached to the signed-in user, or <b>null</b> if the user does not exist or provided an incorrect password
	 */
	IUserSession login(String email, String password);
	
	/**
	 * Manually creates a {@link IUserSession} attached to a specific {@link IUserAccount}
	 * @param account the {@link IUserAccount} being signed into
	 * @return {@link IUserSession} The session attached to the specified user account
	 */
	IUserSession login(IUserAccount account);
	
	/**
	 * Creates a {@link IUserAccount} with a user-input email and password
	 * @param email The email address provided by the user
	 * @param password The password provided by the user
	 * @return {@link IUserAccount} The account representing all the data attached to the user
	 */
	IUserAccount create(String email, String password);
	
	/**
	 * Locates a {@link IUserSession} by a user-provided session token
	 * @param token The session token provided by the user
	 * @return {@link IUserSession} The session assigned to that session token, or <b>null</b> if no such session exists
	 */
	IUserSession getSession(String token);
	
	/**
	 * Logs out of a {@link IUserSession} and destroys it
	 * @param session The session being destroyed
	 */
	void logout(IUserSession session);
	
	/**
	 * Changes the email address associated with a {@link IUserAccount}
	 * @param account The {@link IUserAccount} having their email address changed
	 * @param newEmail The new email being assigned to the {@link IUserAccount}
	 * @param password The password of the {@link IUserAccount} to validate the email change
	 * @return boolean <b>true</b> if the email was successfully changed, <b>false</b> if the provided password was incorrect or the change otherwise failed
	 */
	boolean changeEmail(IUserAccount account, String newEmail, String password);
	
	/**
	 * Changes the password associated with a {@link IUserAccount}
	 * @param account The {@link IUserAccount} having their password changed
	 * @param newPassword The password being assigned to the {@link IUserAccount}
	 * @param password The old password of the {@link IUserAccount} to validate the email change
	 * @return boolean <b>true</b> if the password was successfully changed, <b>false</b> if the provided password was incorrect or the change otherwise failed
	 */
	boolean changePassword(IUserAccount account, String newPassword, String password);
}