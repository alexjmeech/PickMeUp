package org.hacksmu.pickmeup.api;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.hacksmu.pickmeup.postmates.PostmateDelivery;
import org.hacksmu.pickmeup.postmates.PostmateManager;

import spark.Spark;

public class DeliveryManager
{
	private static final Map<Integer, String> ACTIVE_DELIVERIES = new ConcurrentHashMap<>();
	
	static
	{
		new Thread(() ->
		{
			Spark.awaitInitialization();
			while (true)
			{
				try
				{
					Thread.sleep(10000);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				ACTIVE_DELIVERIES.entrySet().removeIf(entry ->
				{
					PostmateDelivery delivery = PostmateManager.getDelivery(entry.getValue());
					
					return delivery == null || delivery.complete;
				});
			}
		}).start();
	}
	
	public static void registerDelivery(int userId, String deliveryId)
	{
		ACTIVE_DELIVERIES.put(userId, deliveryId);
	}
	
	public static PostmateDelivery getDelivery(int userId)
	{
		String deliveryId = ACTIVE_DELIVERIES.get(userId);
		PostmateDelivery delivery = PostmateManager.getDelivery(deliveryId);
		if (delivery == null)
		{
			return null;
		}
		else
		{
			return delivery;
		}
	}
	
	public static void cancelDelivery(String deliveryId)
	{
		ACTIVE_DELIVERIES.entrySet().removeIf(e -> e.getValue().equals(deliveryId));
	}
}