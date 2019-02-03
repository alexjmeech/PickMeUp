package org.hacksmu.pickmeup.api;

public class NumberUtil
{
	public static boolean isInteger(String value)
	{
		try
		{
			Integer.parseInt(value);
			
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
	
	public static boolean isDouble(String value)
	{
		try
		{
			Double.parseDouble(value);
			
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
	
	public static boolean isLong(String value)
	{
		try
		{
			Long.parseLong(value);
			
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
	
	public static boolean isFloat(String value)
	{
		try
		{
			Float.parseFloat(value);
			
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
}