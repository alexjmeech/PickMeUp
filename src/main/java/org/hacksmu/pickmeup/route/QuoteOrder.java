package org.hacksmu.pickmeup.route;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import org.hacksmu.pickmeup.api.DeliveryManager;
import org.hacksmu.pickmeup.api.PickMeUpAuthorizer;
import org.hacksmu.pickmeup.api.account.AccessLevel;
import org.hacksmu.pickmeup.api.account.impl.AccountManager;
import org.hacksmu.pickmeup.api.address.AddressManager;
import org.hacksmu.pickmeup.api.address.UserAddress;
import org.hacksmu.pickmeup.api.payment.CreditCard;
import org.hacksmu.pickmeup.api.payment.PaymentManager;
import org.hacksmu.pickmeup.api.webserver.BasicRoute;
import org.hacksmu.pickmeup.api.webserver.BodyParser;
import org.hacksmu.pickmeup.postmates.PostmateDelivery;
import org.hacksmu.pickmeup.postmates.PostmateManager;
import org.hacksmu.pickmeup.postmates.PostmateQuote;

import spark.Request;
import spark.Response;

public class QuoteOrder extends BasicRoute
{
	public QuoteOrder()
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
			UserAddress address = AddressManager.INSTANCE.getAddress(AccountManager.INSTANCE.getRequestSession(request).getAccountID());
			CreditCard card = PaymentManager.INSTANCE.getCard(AccountManager.INSTANCE.getRequestSession(request).getAccountID());
			Map<String, String> bodyParsed = BodyParser.parseBody(body);
			String storeAddress = bodyParsed.get("store_address");
			String storePhone = bodyParsed.get("store_phone");
			String orderDescription = bodyParsed.get("order_description");
			String orderNotes = bodyParsed.get("order_notes");
			String orderId = bodyParsed.get("order_id");
			String storeName = bodyParsed.get("store_name");
			boolean idRequired = bodyParsed.getOrDefault("id_required", "off").equals("on");
			boolean signatureRequired = bodyParsed.getOrDefault("signature_required", "off").equals("on");
			
			if (address != null && card != null)
			{
				PostmateQuote quote = PostmateManager.getQuote(storeAddress, null, address.getAddress1() + ", " + address.getCity() + ", " + address.getState(), null);
				PostmateDelivery delivery = PostmateManager.createDelivery(quote.id, orderDescription, orderId, storeName, storeAddress, "415-555-8484", orderNotes, address.getFirstName() + " " + address.getLastName(), address.getAddress1() + ", " +  address.getCity() + ", " + address.getState(), "415-555-8484", address.getAddress2(), idRequired, signatureRequired);
				DeliveryManager.registerDelivery(AccountManager.INSTANCE.getRequestSession(request).getAccountID(), delivery.id);
				response.type("text/html");
				request.session(true).attribute("showing_quote", true);
				response.redirect("/orderquote/" + quote.id + "/" + quote.fee + "/" + quote.currency + "/" + quote.duration + "/" + quote.pickup_duration);
				return "";
			}
		}
		response.type("text/html");
		response.redirect("/order");
		return "";
	}
}
