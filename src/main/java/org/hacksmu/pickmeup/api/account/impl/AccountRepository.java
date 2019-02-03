package org.hacksmu.pickmeup.api.account.impl;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.hacksmu.pickmeup.api.account.AccessLevel;
import org.hacksmu.pickmeup.api.account.api.IAccountRepository;
import org.hacksmu.pickmeup.api.account.api.IUserAccount;

public class AccountRepository implements IAccountRepository
{
	private static class UserRow
	{
		private static final AtomicInteger AUTO_INCREMENT_ID = new AtomicInteger(1);
		
		private final int _id;
		private String _email;
		private String _passwordHash;
		private AccessLevel _accessLevel;
		private final Object _lock = new Object();
		
		UserRow(String email, String passwordHash, AccessLevel accessLevel)
		{
			_id = AUTO_INCREMENT_ID.getAndIncrement();
			
			synchronized (_lock)
			{
				_email = email;
				_passwordHash = passwordHash;
				_accessLevel = accessLevel;
			}
		}
		
		public int getId()
		{
			return _id;
		}
		
		public String getEmail()
		{
			synchronized (_lock)
			{
				return _email;
			}
		}
		
		public void setEmail(String email)
		{
			synchronized (_lock)
			{
				_email = email;
			}
		}
		
		public String getPasswordHash()
		{
			synchronized (_lock)
			{
				return _passwordHash;
			}
		}
		
		public void setPasswordHash(String passwordHash)
		{
			synchronized (_lock)
			{
				_passwordHash = passwordHash;
			}
		}
		
		public AccessLevel getAccessLevel()
		{
			synchronized (_lock)
			{
				return _accessLevel;
			}
		}
		
		public void setAccessLevel(AccessLevel accessLevel)
		{
			synchronized (_lock)
			{
				_accessLevel = accessLevel;
			}
		}
	}
	
	private final Map<Integer, UserRow> _accounts = new ConcurrentHashMap<>();
	
	@Override
	public int createAccount(String email, String passwordHash)
	{
		IUserAccount existing = selectAccount(email);
		if (existing != null)
		{
			return existing.getID();
		}
		UserRow row = new UserRow(email, passwordHash, AccessLevel.LOGGED_IN);
		_accounts.put(row.getId(), row);
		
		return row.getId();
	}

	@Override
	public IUserAccount selectAccount(int id)
	{
		UserRow row = _accounts.get(id);
		if (row != null)
		{
			return new UserAccount(row.getId(), row.getEmail(), row.getPasswordHash(), row.getAccessLevel());
		}
		
		return null;
	}

	@Override
	public IUserAccount selectAccount(String email)
	{
		Optional<UserRow> row = _accounts.entrySet().stream().filter(e -> e.getValue().getEmail().equalsIgnoreCase(email)).map(Entry::getValue).findAny();
		if (row.isPresent())
		{
			return new UserAccount(row.get().getId(), row.get().getEmail(), row.get().getPasswordHash(), row.get().getAccessLevel());
		}
		
		return null;
	}

	@Override
	public void updateAccountEmail(int id, String newEmail)
	{
		UserRow row = _accounts.get(id);
		if (row != null)
		{
			row.setEmail(newEmail);
		}
	}

	@Override
	public void updateAccountPassword(int id, String newPasswordHash)
	{
		UserRow row = _accounts.get(id);
		if (row != null)
		{
			row.setPasswordHash(newPasswordHash);
		}
	}
}