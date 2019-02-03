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

}