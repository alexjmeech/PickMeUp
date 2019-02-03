package org.hacksmu.pickmeup.route;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import org.hacksmu.pickmeup.api.NumberUtil;
import org.hacksmu.pickmeup.api.PickMeUpAuthorizer;
import org.hacksmu.pickmeup.api.account.AccessLevel;
import org.hacksmu.pickmeup.api.account.impl.AccountManager;
import org.hacksmu.pickmeup.api.payment.PaymentManager;
import org.hacksmu.pickmeup.api.webserver.BasicRoute;
import org.hacksmu.pickmeup.api.webserver.BodyParser;

import spark.Request;
import spark.Response;

public class SetCard extends BasicRoute
{
	public SetCard()
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
			String cardNumber = bodyParsed.get("cardnumber");
			String expiration = bodyParsed.get("expiration");
			String cvv = bodyParsed.get("cvv");
			String zip = bodyParsed.get("zip");
			
			if (NumberUtil.isInteger(cardNumber) && NumberUtil.isInteger(cvv) && NumberUtil.isInteger(zip))
			{
				PaymentManager.INSTANCE.addCard(AccountManager.INSTANCE.getRequestSession(request).getAccountID(), Integer.valueOf(cardNumber), Integer.valueOf(cvv), expiration, Integer.valueOf(zip));
				
				response.type("text/html");
				response.redirect("/settings");
				return "";
			}
		}
		response.type("text/html");
		response.redirect("/payment");
		return "";
	}
}