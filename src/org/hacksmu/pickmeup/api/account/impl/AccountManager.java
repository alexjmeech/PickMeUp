package org.hacksmu.pickmeup.api.account.impl;
import org.hacksmu.pickmeup.api.webserver.AccessLevel;
import org.hacksmu.pickmeup.api.account.api.IAccountRepository;
import org.hacksmu.pickmeup.api.account.api.IAccountManager;
import org.hacksmu.pickmeup.api.account.api.IUserAccount;
import org.hacksmu.pickmeup.api.account.api.IUserSession;

public class AccountManager implements IAccountManager {
    //private data members
    private IAccountRepository repository;
    //public methods
    @Override
    public IUserSession login(String email, String password) {
        IUserAccount usraccount = repository.selectAccount(email);
        IUserSession session = new UserSession(usraccount.getID(), AccessLevel.NONE);
        return session;
    }
    /**
	 * Manually creates a {@link IUserSession} attached to a specific {@link IUserAccount}
	 * @param account the {@link IUserAccount} being signed into
	 * @return {@link IUserSession} The session attached to the specified user account
	 */
    @Override
    public IUserSession login(IUserAccount account) {
        IUserAccount usraccount = repository.selectAccount(account.getEmail());
        IUserSession session = new UserSession(usraccount.getID(), AccessLevel.NONE);
        return session;
    }
    /**
	 * Creates a {@link IUserAccount} with a user-input email and password
	 * @param email The email address provided by the user
	 * @param password The password provided by the user
	 * @return {@link IUserAccount} The account representing all the data attached to the user
	 */
    @Override
    public IUserAccount create(String email, String password) {

    }
    /**
	 * Locates a {@link IUserSession} by a user-provided session token
	 * @param token The session token provided by the user
	 * @return {@link IUserSession} The session assigned to that session token, or <b>null</b> if no such session exists
	 */
    @Override
    public IUserSession getSession(String token) {

    }
    /**
	 * Logs out of a {@link IUserSession} and destroys it
	 * @param session The session being destroyed
	 */
    @Override
    public void logout(IUserSession session) {

    }
}