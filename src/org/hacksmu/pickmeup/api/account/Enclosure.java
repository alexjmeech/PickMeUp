package org.hacksmu.pickmeup.api.account;

public class Enclosure<T>
{
	private T _enclosed;
	
	public Enclosure()
	{
		_enclosed = null;
	}
	
	public Enclosure(T enclosed)
	{
		_enclosed = enclosed;
	}
	
	public T getEnclosed()
	{
		return _enclosed;
	}
	
	public boolean isEmpty()
	{
		return _enclosed == null;
	}
	
	public void setEnclosed(T enclosed)
	{
		_enclosed = enclosed;
	}
}