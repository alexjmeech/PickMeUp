package org.hacksmu.pickmeup.api.payment;

public class CreditCard
{
	private final int _cardNumber;
	private final int _cvv;
	private final String _expiration;
	private final int _zip;
	
	public CreditCard(int cardNumber, int cvv, String expiration, int zip)
	{
		_cardNumber = cardNumber;
		_cvv = cvv;
		_expiration = expiration;
		_zip = zip;
	}
	
	public int getCardNumber()
	{
		return _cardNumber;
	}
	
	public int getCVV()
	{
		return _cvv;
	}
	
	public String getExpiration()
	{
		return _expiration;
	}
	
	public int getZipCode()
	{
		return _zip;
	}
}