package org.hacksmu.pickmeup.api.payment;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PaymentRepository
{
	private static class CardRow
	{
		private final int _userId;
		private final int _cardNumber;
		private final int _cvv;
		private final String _expiration;
		private final int _zip;
		
		CardRow(int userId, int cardNumber, int cvv, String expiration, int zip)
		{
			_userId = userId;
			_cardNumber = cardNumber;
			_cvv = cvv;
			_expiration = expiration;
			_zip = zip;
		}
		
		public int getUserID()
		{
			return _userId;
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
		
		public int getZip()
		{
			return _zip;
		}
	}
	
	private final Map<Integer, CardRow> _cards = new ConcurrentHashMap<>();
	
	public void createCard(int userId, int cardNumber, int cvv, String expiration, int zip)
	{
		CardRow row = new CardRow(userId, cardNumber, cvv, expiration, zip);
		_cards.put(userId, row);
	}
	
	public CreditCard selectCard(int userId)
	{
		CardRow row = _cards.get(userId);
		if (row == null)
		{
			return null;
		}
		
		return new CreditCard(row.getCardNumber(), row.getCVV(), row.getExpiration(), row.getZip());
	}
}