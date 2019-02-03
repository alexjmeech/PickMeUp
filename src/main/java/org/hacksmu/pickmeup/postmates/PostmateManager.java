package org.hacksmu.pickmeup.postmates;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class PostmateManager {
	private static String APIkey = "c41bb275-4ecc-40dd-ac09-0c29f99a1d00";
	private static String customerID = "cus_M4M4bpoORyPoGk";

	public static PostmateQuote getQuote(String pickup_address, String pickup_phone_number, String dropoff_address, String dropoff_phone_number) {
		Map<String, String> params = new HashMap<>();
		params.put("pickup_address", pickup_address);
		params.put("dropoff_address", dropoff_address);
		if (dropoff_phone_number != null)
			params.put("dropoff_phone_number", dropoff_phone_number);
		if (pickup_phone_number != null)
			params.put("pickup_phone_number", pickup_phone_number);
		try {
			PostmateQuote quote = new PostmatesWebCall<PostmateQuote> (APIkey, "POST", customerID, "customers/:customer_id/delivery_quotes").call(PostmateQuote.class, params);

			return quote;
		} catch(IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static PostmateDelivery createDelivery(String quote_id, String manifest, String manifest_reference, String pickup_name, String pickup_address, String pickup_phone_number, String pickup_notes, String dropoff_name, String dropoff_address, String dropoff_phone_number, String dropoff_notes, boolean requires_id, boolean requires_dropoff_signature)
	{
		Map<String, String> params = new HashMap<>();
		params.put("quote_id", quote_id);
		params.put("manifest", manifest);
		params.put("manifest_reference", manifest_reference);
		params.put("pickup_name", pickup_name);
		params.put("pickup_address", pickup_address);
		if (pickup_phone_number != null)
			params.put("pickup_phone_number", pickup_phone_number);
		params.put("pickup_notes", pickup_notes);
		params.put("dropoff_name", dropoff_name);
		params.put("dropoff_address", dropoff_address);
		if (dropoff_phone_number != null)
			params.put("dropoff_phone_number", dropoff_phone_number);
		params.put("dropoff_notes", dropoff_notes);
		if (requires_id && false)
			params.put("requires_id", String.valueOf(requires_id));
		if (requires_dropoff_signature && false)
			params.put("requires_dropoff_signature", String.valueOf(requires_dropoff_signature));

		try {
			PostmateDelivery delivery = new PostmatesWebCall<PostmateDelivery> (APIkey, "POST", customerID, "customers/:customer_id/deliveries").call(PostmateDelivery.class, params);

			return delivery;
		} catch(IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static PostmateDelivery getDelivery(String deliveryId)
	{
		try {
			PostmateDelivery delivery = new PostmatesWebCall<PostmateDelivery>(APIkey, "GET", customerID, "customers/:customer_id/deliveries/" + deliveryId).call(PostmateDelivery.class, null);
			return delivery;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void cancelDelivery(String deliveryId)
	{
		try {
			Object obj = new PostmatesWebCall<Object>(APIkey, "GET", customerID, "customers/:customer_id/deliveries/" + deliveryId + "/cancel").call(Object.class, new HashMap<>());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}