package org.hacksmu.pickmeup.route.account;

import org.hacksmu.pickmeup.api.ObjectDebugUtil;
import org.hacksmu.pickmeup.api.account.AccessLevel;
import org.hacksmu.pickmeup.api.webserver.BasicRoute;

import spark.Request;
import spark.Response;

public class CreateAccount extends BasicRoute
{
	public CreateAccount()
	{
		super(AccessLevel.NONE);
	}

	@Override
	protected String handleRequest (Request request, Response response)
	{
		System.out.println("BODY: " + request.body());
		response.type("text/html");
		response.redirect("/login");
		return "";
	}
}