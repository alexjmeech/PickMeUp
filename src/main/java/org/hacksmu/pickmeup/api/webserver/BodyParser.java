package org.hacksmu.pickmeup.api.webserver;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.google.common.collect.ImmutableMap;

public class BodyParser
{
	public static Map<String, String> parseBody(String body)
	{
		Map<String, String> bodyMap = new HashMap<>();
		String[] elements = body.split(Pattern.quote("&"));
		for (String element : elements)
		{
			String[] keyValue = element.split(Pattern.quote("="));
			String key = keyValue[0];
			if (keyValue.length >= 2)
			{
				String value = keyValue[1];
				bodyMap.put(key, value);
			}
			else
			{
				bodyMap.put(key, "");
			}
		}
		
		return ImmutableMap.copyOf(bodyMap);
	}
}