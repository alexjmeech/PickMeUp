package org.hacksmu.pickmeup.api.address;

import org.hacksmu.pickmeup.api.account.api.IUserAccount;

public class AddressManager
{
	public static final AddressManager INSTANCE = new AddressManager();
	
	private final AddressRepository repository = new AddressRepository();
	
	private AddressManager() {}
	
	public UserAddress getAddress(IUserAccount account)
	{
		return repository.selectAddress(account.getID());
	}
	
	public UserAddress getAddress(int userId)
	{
		return repository.selectAddress(userId);
	}
	
	public void addAddress(IUserAccount account, String address1, String address2, String city, String state, int zip, String firstName, String lastName)
	{
		repository.createAddress(account.getID(), address1, address2, city, state, zip, firstName, lastName);
	}
	
	public void addAddress(int userId, String address1, String address2, String city, String state, int zip, String firstName, String lastName)
	{
		repository.createAddress(userId, address1, address2, city, state, zip, firstName, lastName);
	}
}