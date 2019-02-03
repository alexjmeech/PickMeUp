package org.hacksmu.pickmeup.api.address;

public class UserAddress
{
	private final String _address1;
	private final String _address2;
	private final String _city;
	private final String _state;
	private final int _zip;
	private final String _firstName;
	private final String _lastName;
	
	public UserAddress(String address1, String address2, String city, String state, int zip, String firstName, String lastName)
	{
		_address1 = address1;
		_address2 = address2;
		_city = city;
		_state = state;
		_zip = zip;
		_firstName = firstName;
		_lastName = lastName;
	}
	
	public String getAddress1()
	{
		return _address1;
	}
	
	public String getAddress2()
	{
		return _address2;
	}
	
	public String getCity()
	{
		return _city;
	}
	
	public String getState()
	{
		return _state;
	}
	
	public int getZipCode()
	{
		return _zip;
	}
	
	public String getFirstName()
	{
		return _firstName;
	}
	
	public String getLastName()
	{
		return _lastName;
	}
}