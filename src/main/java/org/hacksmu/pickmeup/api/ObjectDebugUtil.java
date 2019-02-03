package org.hacksmu.pickmeup.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ObjectDebugUtil
{
	private static final Gson GSON = new GsonBuilder()
			.disableHtmlEscaping()
			.serializeNulls()
			.serializeSpecialFloatingPointValues()
			.create();
	
	public static void debugObject(Object o)
	{
		System.out.println("DEBUGGING OBJECT: " + o.getClass().getName());
		System.out.println(GSON.toJson(o));
	}
}