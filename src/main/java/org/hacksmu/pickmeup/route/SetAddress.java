package org.hacksmu.pickmeup.route;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import org.hacksmu.pickmeup.api.NumberUtil;
import org.hacksmu.pickmeup.api.PickMeUpAuthorizer;
import org.hacksmu.pickmeup.api.account.AccessLevel;
import org.hacksmu.pickmeup.api.account.impl.AccountManager;
import org.hacksmu.pickmeup.api.address.AddressManager;
import org.hacksmu.pickmeup.api.webserver.BasicRoute;
import org.hacksmu.pickmeup.api.webserver.BodyParser;

import spark.Request;
import spark.Response;

public class SetAddress extends BasicRoute
{
	public SetAddress()
	{
		super(AccessLevel.NONE);
	}

	@Override
	protected String handleRequest (Request request, Response response)
	{
		if (!PickMeUpAuthorizer.authorized(request, AccessLevel.LOGGED_IN))
		{
			response.type("text/html");
			response.redirect("/login");
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
			String address1 = bodyParsed.get("address_1");
			String address2 = bodyParsed.get("address_2");
			String city = bodyParsed.get("city");
			String state = bodyParsed.get("state");
			String zip = bodyParsed.get("zip");
			String firstName = bodyParsed.get("first_name");
			String lastName = bodyParsed.get("last_name");
			
			if (NumberUtil.isInteger(zip))
			{
				AddressManager.INSTANCE.addAddress(AccountManager.INSTANCE.getRequestSession(request).getAccountID(), address1, address2, city, state, Integer.valueOf(zip), firstName, lastName);
				
				response.type("text/html");
				response.redirect("/settings");
				return "";
			}
		}
		response.type("text/html");
		response.redirect("/address");
		return "";
	}
}