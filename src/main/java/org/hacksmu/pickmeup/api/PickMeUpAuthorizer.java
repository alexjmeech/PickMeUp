package org.hacksmu.pickmeup.api;

import org.hacksmu.pickmeup.api.account.AccessLevel;
import org.hacksmu.pickmeup.api.account.api.IAccountManager;
import org.hacksmu.pickmeup.api.account.api.IUserSession;

import spark.Filter;
import spark.Request;
import spark.Session;

public class PickMeUpAuthorizer
{
	private static final IAccountManager MANAGER;
	
	static
	{
		MANAGER = null;
	}
	
	public static Filter getInitialFilter()
	{
		return (request, response) ->
		{
			if (request.session(false) == null)
			{
				return;
			}
			
			String token = request.session(false).attribute("sessionToken");
			
			if (token != null && MANAGER.getSession(token) == null)
			{
				request.session(false).removeAttribute("sessionToken");
			}
		};
	}
	
	public static boolean authorized(Request request, AccessLevel level)
	{
		Session session = request.session(false);
		if (session == null)
		{
			return level == AccessLevel.NONE;
		}
		String token = request.session(false).attribute("sessionToken");
		if (token == null)
		{
			return level == AccessLevel.NONE;
		}
		
		IUserSession userSession = MANAGER.getSession(request.session(false).attribute("sessionToken"));
		if (userSession == null)
		{
			return level == AccessLevel.NONE;
		}
		
		return userSession.getAccessLevel().ordinal() <= level.ordinal();
	}
}