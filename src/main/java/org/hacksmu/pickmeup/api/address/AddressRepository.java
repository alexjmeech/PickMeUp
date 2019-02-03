package org.hacksmu.pickmeup.api.address;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AddressRepository
{
	private static class AddressRow
	{
		private final int _userId;
		private final String _address1;
		private final String _address2;
		private final String _city;
		private final String _state;
		private final int _zip;
		private final String _firstName;
		private final String _lastName;
		
		AddressRow(int userId, String address1, String address2, String city, String state, int zip, String firstName, String lastName)
		{
			_userId = userId;
			_address1 = address1;
			_address2 = address2;
			_city = city;
			_state = state;
			_zip = zip;
			_firstName = firstName;
			_lastName = lastName;
		}
		
		public int getUserID()
		{
			return _userId;
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
	
	private final Map<Integer, AddressRow> _addresses = new ConcurrentHashMap<>();
	
	public void createAddress(int userId, String address1, String address2, String city, String state, int zip, String firstName, String lastName)
	{
		AddressRow row = new AddressRow(userId, address1, address2, city, state, zip, firstName, lastName);
		_addresses.put(userId, row);
	}
	
	public UserAddress selectAddress(int userId)
	{
		AddressRow row = _addresses.get(userId);
		if (row == null)
		{
			return null;
		}
		
		return new UserAddress(row.getAddress1(), row.getAddress2(), row.getCity(), row.getState(), row.getZipCode(), row.getFirstName(), row.getLastName());
	}
}