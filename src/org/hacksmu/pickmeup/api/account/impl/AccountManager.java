package org.hacksmu.pickmeup.api.account.impl;
import java.util.concurrent.TimeUnit;

import org.hacksmu.pickmeup.api.account.AccessLevel;
import org.hacksmu.pickmeup.api.account.api.IAccountManager;
import org.hacksmu.pickmeup.api.account.api.IAccountRepository;
import org.hacksmu.pickmeup.api.account.api.IUserAccount;
import org.hacksmu.pickmeup.api.account.api.IUserSession;
import org.hacksmu.pickmeup.api.account.api.PasswordHash;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class AccountManager implements IAccountManager {
    //private data members
    private IAccountRepository repository;
    private final Cache<String, UserSession> tokens = CacheBuilder.newBuilder().expireAfterWrite(3, TimeUnit.HOURS).build();
    //public methods
    @Override
    public IUserSession login(String email, String password) {
        IUserAccount usraccount = repository.selectAccount(email);
        if (PasswordHash.safeValidatePassword(password, usraccount.getPasswordHash()))
        {
        	IUserSession session = new UserSession(usraccount.getID(), AccessLevel.NONE);
        	return session;
        }
        else
        {
        	return null;
        }
    }
    @Override
    public IUserSession login(IUserAccount account) {
        IUserAccount usraccount = repository.selectAccount(account.getEmail());
        IUserSession session = new UserSession(usraccount.getID(), usraccount.getAccessLevel());
        return session;
    }
    @Override
    public IUserAccount create(String email, String password) {
    	try
    	{
    		String passwordHash = PasswordHash.createHash(password);
    		int id = repository.createAccount(email, passwordHash);
    		IUserAccount usraccount = new UserAccount(id, email, passwordHash, AccessLevel.NONE);
    		return usraccount;
    	}
    	catch (Exception ex)
    	{
    		ex.printStackTrace();
    		return null;
    	}
    }
    /**
	 * Locates a {@link IUserSession} by a user-provided session token
	 * @param token The session token provided by the user
	 * @return {@link IUserSession} The session assigned to that session token, or <b>null</b> if no such session exists
	 */
    @Override
    public IUserSession getSession(String token) {
    	return tokens.getIfPresent(token);
    }
    /**
	 * Logs out of a {@link IUserSession} and destroys it
	 * @param session The session being destroyed
	 */
    @Override
    public void logout(IUserSession session) {
    	tokens.invalidate(session.getToken());
    }
	@Override
	public boolean changeEmail(IUserAccount account, String newEmail, String password) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean changePassword(String email, String newPassword, String password) {
		// TODO Auto-generated method stub
		return false;
	}
}