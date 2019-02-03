package org.hacksmu.pickmeup.route;

import org.hacksmu.pickmeup.api.PickMeUpAuthorizer;
import org.hacksmu.pickmeup.api.account.AccessLevel;
import org.hacksmu.pickmeup.api.webserver.BasicRoute;

import spark.Request;
import spark.Response;

public class HandleLogout extends BasicRoute
{
	public HandleLogout()
	{
		super(AccessLevel.NONE);
	}

	@Override
	protected String handleRequest(Request request, Response response)
	{
		if (!PickMeUpAuthorizer.authorized(request, AccessLevel.LOGGED_IN))
		{
			response.type("text/html");
			response.redirect("/login");
			return "";
		}
		
		if (request.session(false) != null)
		{
			request.session(false).removeAttribute("sessionToken");
		}
		
		response.type("text/html");
		response.redirect("/login");
		return "";
	}
}