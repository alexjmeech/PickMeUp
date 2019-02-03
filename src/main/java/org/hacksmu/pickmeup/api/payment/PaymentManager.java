package org.hacksmu.pickmeup.api.payment;

import org.hacksmu.pickmeup.api.account.api.IUserAccount;

public class PaymentManager
{
	public static final PaymentManager INSTANCE = new PaymentManager();
	
	private final PaymentRepository repository = new PaymentRepository();
	
	private PaymentManager() {}
	
	public CreditCard getCard(IUserAccount account)
	{
		return repository.selectCard(account.getID());
	}
	
	public CreditCard getCard(int userId)
	{
		return repository.selectCard(userId);
	}
	
	public void addCard(IUserAccount account, int cardNumber, int cvv, String expiration, int zip)
	{
		repository.createCard(account.getID(), cardNumber, cvv, expiration, zip);
	}
	
	public void addCard(int userId, int cardNumber, int cvv, String expiration, int zip)
	{
		repository.createCard(userId, cardNumber, cvv, expiration, zip);
	}
}