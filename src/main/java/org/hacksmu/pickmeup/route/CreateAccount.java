package org.hacksmu.pickmeup.route;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import org.hacksmu.pickmeup.api.PickMeUpAuthorizer;
import org.hacksmu.pickmeup.api.account.AccessLevel;
import org.hacksmu.pickmeup.api.account.api.IUserSession;
import org.hacksmu.pickmeup.api.account.impl.AccountManager;
import org.hacksmu.pickmeup.api.webserver.BasicRoute;
import org.hacksmu.pickmeup.api.webserver.BodyParser;

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
		if (PickMeUpAuthorizer.authorized(request, AccessLevel.LOGGED_IN))
		{
			response.type("text/html");
			response.redirect("/dashboard");
			return "";
		}
		String body = request.body();
		if (body != null && !body.isEmpty())
		{
			try
			{
				body = URLDecoder.decode(body, "UTF-8");
			}
			catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
			Map<String, String> bodyParsed = BodyParser.parseBody(body);
			String email = bodyParsed.get("email");
			String password = bodyParsed.get("password");
			String password_confirm = bodyParsed.get("password_confirm");
			
			if (password.equals(password_confirm))
			{
				IUserSession session = AccountManager.INSTANCE.login(AccountManager.INSTANCE.create(email, password));
				request.session(true).attribute("sessionToken", session.getToken());
				
				
				response.type("text/html");
				response.redirect("/dashboard");
				return "";
			}
		}
		response.type("text/html");
		response.redirect("/signup");
		return "";
	}
}