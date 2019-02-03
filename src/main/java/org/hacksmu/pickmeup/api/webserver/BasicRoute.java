package org.hacksmu.pickmeup.api.webserver;

import static spark.Spark.halt;

import org.hacksmu.pickmeup.api.PickMeUpAuthorizer;
import org.hacksmu.pickmeup.api.account.AccessLevel;

import com.google.gson.Gson;

import spark.Request;
import spark.Response;
import spark.Route;

public abstract class BasicRoute implements Route
{
	private static final Gson GSON = new Gson();
	private final AccessLevel _requiredLevel;

	public BasicRoute(AccessLevel level)
	{
		_requiredLevel = level;
	}

	protected abstract String handleRequest(Request request, Response response);

	protected String serialize(Object object)
	{
		return GSON.toJson(object);
	}

	@Override
	public final String handle(Request request, Response response)
	{
		response.type("application/json");
//		if (!PickMeUpAuthorizer.authorized(request, _requiredLevel))
//		{
//			halt(401,
//					"{\"statusCode\":\"401\",\"error\":\"Permission Denied\",\"message\":\"Invalid"
//							+ " Authorization for that request\"}");
//			return null;
//		}
		
		return handleRequest(request, response);
	}
}