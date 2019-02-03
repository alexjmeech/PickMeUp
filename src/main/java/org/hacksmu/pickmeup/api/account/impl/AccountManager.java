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

import spark.Request;

public class AccountManager implements IAccountManager
{	
	public static final AccountManager INSTANCE = new AccountManager();
	
    //private data members
    private final IAccountRepository repository = new AccountRepository();
    private final Cache<String, IUserSession> tokens = CacheBuilder.newBuilder().expireAfterWrite(3, TimeUnit.HOURS).build();
    //public methods
    
    private AccountManager() {}
    
    @Override
    public IUserSession login(String email, String password)
    {
        IUserAccount usraccount = repository.selectAccount(email);
        if (PasswordHash.safeValidatePassword(password, usraccount.getPasswordHash()))
        {
        	IUserSession session = new UserSession(usraccount.getID(), usraccount.getAccessLevel());
        	tokens.put(session.getToken(), session);
        	return session;
        }
        else
        {
        	return null;
        }
    }
    @Override
    public IUserSession login(IUserAccount account)
    {
        IUserSession session = new UserSession(account.getID(), account.getAccessLevel());
        tokens.put(session.getToken(), session);
        return session;
    }
    @Override
    public IUserAccount create(String email, String password)
    {
    	try
    	{
    		String passwordHash = PasswordHash.createHash(password);
    		int id = repository.createAccount(email, passwordHash);
    		IUserAccount usraccount = new UserAccount(id, email, passwordHash, AccessLevel.LOGGED_IN);
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
    public IUserSession getSession(String token)
    {
    	return tokens.getIfPresent(token);
    }
    
    public IUserSession getRequestSession(Request request)
    {
    	if (request.session(false) == null)
		{
			return null;
		}
		
		String token = request.session(false).attribute("sessionToken");
		
		if (token != null)
		{
			return getSession(token);
		}
		
		return null;
    }
    
    /**
	 * Logs out of a {@link IUserSession} and destroys it
	 * @param session The session being destroyed
	 */
    @Override
    public void logout(IUserSession session)
    {
    	tokens.invalidate(session.getToken());
    }
    
	@Override
	public boolean changeEmail(IUserAccount account, String newEmail, String password)
	{
		try
    	{
			if (PasswordHash.safeValidatePassword(password, account.getPasswordHash()))
			{
				account.setEmail(newEmail);
				repository.updateAccountEmail(account.getID(), newEmail);
				return true;
			}
    	}
    	catch (Exception ex)
    	{
    		ex.printStackTrace();
    		return false;
    	}
		return false;
	}
	/**
	 * Changes the password associated with a {@link IUserAccount}
	 * @param account The {@link IUserAccount} having their password changed
	 * @param newPassword The password being assigned to the {@link IUserAccount}
	 * @param password The old password of the {@link IUserAccount} to validate the email change
	 * @return boolean <b>true</b> if the password was successfully changed, <b>false</b> if the provided password was incorrect or the change otherwise failed
	 */
	@Override
	public boolean changePassword(IUserAccount account, String newPassword, String password)
	{
		try
		{
			if (PasswordHash.safeValidatePassword(password, account.getPasswordHash()))
			{
				account.setPasswordHash(newPassword);
				return true;
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
		return false;
	}
}