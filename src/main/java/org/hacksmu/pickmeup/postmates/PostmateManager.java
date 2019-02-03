package org.hacksmu.pickmeup.postmates;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;



public class PostmateManager {
    private static String APIkey = "c41bb275-4ecc-40dd-ac09-0c29f99a1d00";
    private static String customerID = "cus_M4M4bpoORyPoGk";

    public static PostmateQuote getQuote(String pickup_address, String pickup_phone_number, String dropoff_address, String dropoff_phone_number) {
        Map<String, String> params = new HashMap<>();
        params.put(
            "pickup_address", pickup_address);
        params.put("dropoff_address", dropoff_address);
        params.put("pickup_address", pickup_address);
        params.put("dropoff_phone_number", dropoff_phone_number);
        params.put("pickup_phone_number", pickup_phone_number);
        try {
        PostmateQuote quote = new PostmatesWebCall<PostmateQuote> (APIkey, "POST", customerID, "customers/:customer_id/delivery_quotes").call(PostmateQuote.class, params);

        return quote;
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static PostmateDelivery createDelivery(String quote_id, String manifest, String manifest_reference, String pickup_name, String pickup_address, String pickup_phone_number, String pickup_notes, String dropoff_name, String dropoff_address, String dropoff_phone_number, String dropoff_notes, boolean requires_id, boolean requires_dropoff_signature, String pickup_ready_dt, String pickup_deadline_dt, String dropoff_ready_dt, String dropoff_deadline_dt)
    {
    	Map<String, String> params = new HashMap<>();
    	params.put("quote_id", quote_id);
    	params.put("manifest", manifest);
    	params.put("manifest_reference", manifest_reference);
    	params.put("pickup_name", pickup_name);
    	params.put("pickup_address", pickup_address);
    	params.put("pickup_phone_number", pickup_phone_number);
    	params.put("pickup_notes", pickup_notes);
    	params.put("dropoff_name", dropoff_name);
    	params.put("dropoff_address", dropoff_address);
    	params.put("dropoff_phone_number", dropoff_phone_number);
    	params.put("dropoff_notes", dropoff_notes);
    	params.put("requires_id", String.valueOf(requires_id));
    	params.put("requires_dropoff_signature", String.valueOf(requires_dropoff_signature));
    	params.put("pickup_ready_dt", pickup_ready_dt);
    	params.put("pickup_deadline_dt", pickup_deadline_dt);
    	params.put("dropoff_ready_dt", dropoff_ready_dt);
    	params.put("dropoff_deadline_dt", dropoff_deadline_dt);
    	
    	try {
            PostmateDelivery delivery = new PostmatesWebCall<PostmateDelivery> (APIkey, "POST", customerID, "customers/:customer_id/deliveries").call(PostmateDelivery.class, params);

            return delivery;
            } catch(IOException e) {
                e.printStackTrace();
                return null;
            }
    }

}