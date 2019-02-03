package org.hacksmu.pickmeup;

import static spark.Spark.get;
import static spark.Spark.internalServerError;
import static spark.Spark.notFound;
import static spark.Spark.post;
import static spark.Spark.before;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hacksmu.pickmeup.api.DeliveryManager;
import org.hacksmu.pickmeup.api.NumberUtil;
import org.hacksmu.pickmeup.api.PickMeUpAuthorizer;
import org.hacksmu.pickmeup.api.account.AccessLevel;
import org.hacksmu.pickmeup.api.account.impl.AccountManager;
import org.hacksmu.pickmeup.api.address.AddressManager;
import org.hacksmu.pickmeup.api.address.StateNameMapping;
import org.hacksmu.pickmeup.api.address.UserAddress;
import org.hacksmu.pickmeup.api.payment.CreditCard;
import org.hacksmu.pickmeup.api.payment.PaymentManager;
import org.hacksmu.pickmeup.postmates.PostmateDelivery;
import org.hacksmu.pickmeup.postmates.PostmateManager;
import org.hacksmu.pickmeup.route.CreateAccount;
import org.hacksmu.pickmeup.route.HandleLogin;
import org.hacksmu.pickmeup.route.HandleLogout;
import org.hacksmu.pickmeup.route.QuoteOrder;
import org.hacksmu.pickmeup.route.SetAddress;
import org.hacksmu.pickmeup.route.SetCard;

import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

public class Main
{
	public static void main(String[] args)
	{
		{
			int port = 8080;

			try
			{
				File configFile = new File(new File(".").getCanonicalPath() + File.separator + "config.dat");
				if (configFile.exists())
				{
					List<String> lines = Files.readAllLines(configFile.toPath(), Charset.defaultCharset());

					for (String line : lines)
					{
						if (line.startsWith("#"))
						{
							continue;
						}

						String[] split = line.split(" ");

						if (split.length > 1)
						{
							String key = split[0];
							String value = split[1];

							if (key.equalsIgnoreCase("port") && NumberUtil.isInteger(value))
							{
								port = Integer.parseInt(value);
							}
						}
					}
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}

			Spark.port(port);
		}

		internalServerError((req, res) ->
		{
			res.type("application/json");
			return "{\"statusCode\":\"500\",\"error\":\"Internal Server Error\",\"message\":\"An internal server error occurred\"}";
		});
		notFound((req, res) ->
		{
			res.type("application/json");
			return "{\"statusCode\":\"404\",\"error\":\"Not Found\"}";
		});

		Spark.staticFileLocation("pub");

		before(PickMeUpAuthorizer.getInitialFilter());

		get("/", (request, response) ->
		{
			Map<String, Object> model = new HashMap<>();
			return new VelocityTemplateEngine().render(
					new ModelAndView(model, "Homepage.vm")
					);
		});
		get("/login", (request, response) ->
		{
			if (PickMeUpAuthorizer.authorized(request, AccessLevel.LOGGED_IN))
			{
				response.type("text/html");
				response.redirect("/dashboard");
				return "";
			}
			Map<String, Object> model = new HashMap<>();
			return new VelocityTemplateEngine().render(
					new ModelAndView(model, "Sign_In.vm")
					);
		});
		post("/login", new HandleLogin());
		get("/dashboard", (request, response) ->
		{
			if (!PickMeUpAuthorizer.authorized(request, AccessLevel.LOGGED_IN))
			{
				response.type("text/html");
				response.redirect("/login");
				return "";
			}
			Map<String, Object> model = new HashMap<>();
			return new VelocityTemplateEngine().render(
					new ModelAndView(model, "Signed_In_Home.vm")
					);
		});
		get("/settings", (request, response) ->
		{
			if (!PickMeUpAuthorizer.authorized(request, AccessLevel.LOGGED_IN))
			{
				response.type("text/html");
				response.redirect("/login");
				return "";
			}
			Map<String, Object> model = new HashMap<>();
			return new VelocityTemplateEngine().render(
					new ModelAndView(model, "Settings.vm")
					);
		});
		get("/payment", (request, response) ->
		{
			if (!PickMeUpAuthorizer.authorized(request, AccessLevel.LOGGED_IN))
			{
				response.type("text/html");
				response.redirect("/login");
				return "";
			}
			CreditCard card = PaymentManager.INSTANCE.getCard(AccountManager.INSTANCE.getRequestSession(request).getAccountID());
			Map<String, Object> model = new HashMap<>();
			if (card != null)
			{
				model.put("card", card);
			}
			return new VelocityTemplateEngine().render(
					new ModelAndView(model, "Credit_Card.vm")
					);
		});
		get("/address", (request, response) ->
		{
			if (!PickMeUpAuthorizer.authorized(request, AccessLevel.LOGGED_IN))
			{
				response.type("text/html");
				response.redirect("/login");
				return "";
			}
			UserAddress address = AddressManager.INSTANCE.getAddress(AccountManager.INSTANCE.getRequestSession(request).getAccountID());
			Map<String, Object> model = new HashMap<>();
			if (address != null)
			{
				model.put("address", address);
			}
			model.put("stateMapping", StateNameMapping.createStateMapping());
			return new VelocityTemplateEngine().render(
					new ModelAndView(model, "Address.vm")
					);
		});
		get("/signup", (request, response) ->
		{
			if (PickMeUpAuthorizer.authorized(request, AccessLevel.LOGGED_IN))
			{
				response.type("text/html");
				response.redirect("/dashboard");
				return "";
			}
			Map<String, Object> model = new HashMap<>();
			return new VelocityTemplateEngine().render(
					new ModelAndView(model, "Sign_Up.vm")
					);
		});
		post("/signup", new CreateAccount());

		get("/logout", new HandleLogout());

		post("/setcard", new SetCard());

		post("/setaddress", new SetAddress());

		get("/order", (request, response) ->
		{
			if (!PickMeUpAuthorizer.authorized(request, AccessLevel.LOGGED_IN))
			{
				response.type("text/html");
				response.redirect("/login");
				return "";
			}
			UserAddress address = AddressManager.INSTANCE.getAddress(AccountManager.INSTANCE.getRequestSession(request).getAccountID());
			CreditCard card = PaymentManager.INSTANCE.getCard(AccountManager.INSTANCE.getRequestSession(request).getAccountID());

			if (address == null)
			{
				response.type("text/html");
				response.redirect("/address");
				return "";
			}
			if (card == null)
			{
				response.type("text/html");
				response.redirect("/payment");
				return "";
			}

			Map<String, Object> model = new HashMap<>();
			return new VelocityTemplateEngine().render(
					new ModelAndView(model, "Order.vm")
					);
		});

		get("/deliverymonitor", (request, response) ->
		{
			if (!PickMeUpAuthorizer.authorized(request, AccessLevel.LOGGED_IN))
			{
				response.type("text/html");
				response.redirect("/login");
				return "";
			}

			PostmateDelivery delivery = DeliveryManager.getDelivery(AccountManager.INSTANCE.getRequestSession(request).getAccountID());
			if (delivery != null)
			{
				response.type("text/html");
				response.redirect(delivery.tracking_url);
				return "";
			}
			else
			{
				response.type("text/html");
				response.redirect("/order");
				return "";
			}
		});

		get("/cancelorder", (request, response) ->
		{
			if (!PickMeUpAuthorizer.authorized(request, AccessLevel.LOGGED_IN))
			{
				response.type("text/html");
				response.redirect("/login");
				return "";
			}

			PostmateDelivery delivery = DeliveryManager.getDelivery(AccountManager.INSTANCE.getRequestSession(request).getAccountID());
			if (delivery != null)
			{
				PostmateManager.cancelDelivery(delivery.id);
				DeliveryManager.cancelDelivery(delivery.id);
			}

			response.type("text/html");
			response.redirect("/order");
			return "";
		});

		post("/quoteorder", new QuoteOrder());

		get("/orderquote/:quoteId/:fee/:currency/:duration/:pickupDuration", (request, response) ->
		{
			if (!PickMeUpAuthorizer.authorized(request, AccessLevel.LOGGED_IN))
			{
				response.type("text/html");
				response.redirect("/login");
				return "";
			}
			if (request.session(false).attribute("showing_quote") == null)
			{
				response.type("text/html");
				response.redirect("/order");
				return "";
			}
			request.session(false).removeAttribute("showing_quote");
			Map<String, Object> model = new HashMap<>();

			model.put("quoteId", request.params("quoteId"));
			model.put("cost", "$" + (Double.valueOf(request.params("fee"))/100) + " " + request.params("currency"));
			model.put("deliveryDuration", Integer.valueOf(request.params("duration")));
			model.put("pickupDelay", Integer.valueOf(request.params("pickupDuration")));

			return new VelocityTemplateEngine().render(
					new ModelAndView(model, "OrderQuote.vm")
					);
		});
	}
}