package org.hacksmu.pickmeup.api;

import org.hacksmu.pickmeup.api.account.AccessLevel;
import org.hacksmu.pickmeup.api.account.api.IUserSession;
import org.hacksmu.pickmeup.api.account.impl.AccountManager;

import spark.Filter;
import spark.Request;
import spark.Session;

public class PickMeUpAuthorizer
{
	public static Filter getInitialFilter()
	{
		return (request, response) ->
		{
			if (request.session(false) == null)
			{
				return;
			}
			
			String token = request.session(false).attribute("sessionToken");
			
			if (token != null && AccountManager.INSTANCE.getSession(token) == null)
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
		
		IUserSession userSession = AccountManager.INSTANCE.getSession(request.session(false).attribute("sessionToken"));
		if (userSession == null)
		{
			return level == AccessLevel.NONE;
		}
		
		return userSession.getAccessLevel().ordinal() <= level.ordinal();
	}
}