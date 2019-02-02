package org.hacksmu.pickmeup.api.account.impl;

import java.sql.SQLException;

import org.hacksmu.pickmeup.api.account.AccessLevel;
import org.hacksmu.pickmeup.api.account.Enclosure;
import org.hacksmu.pickmeup.api.account.api.IAccountRepository;
import org.hacksmu.pickmeup.api.account.api.IUserAccount;
import org.hacksmu.pickmeup.api.database.BasicRepository;
import org.hacksmu.pickmeup.api.database.DBPool;

public class AccountRepository extends BasicRepository implements IAccountRepository
{
	private static final String CREATE_TABLE = "CREATE TABLE accounts (id INT AUTO_INCREMENT, email VARCHAR(50) NOT NULL, password_hash VARCHAR(200) NOT NULL, access_level VARCHAR(20) NOT NULL, PRIMARY KEY (id), UNIQUE INDEX email_index (email), INDEX level_index (access_level));";
	private static final String CREATE_ACCOUNT = "INSERT INTO accounts (email, passwordHash) VALUES (?, ?, ?);";
	private static final String SELECT_ACCOUNT_ID = "SELECT * FROM accounts WHERE id=?;";
	private static final String SELECT_ACCOUNT_EMAIL = "SELECT * FROM accounts WHERE email=?;";
	private static final String UPDATE_ACCOUNT_EMAIL = "UPDATE accounts SET email=? WHERE id=?;";
	private static final String UPDATE_ACCOUNT_PASSWORD = "UPDATE accounts SET passwordHash=? WHERE id=?;";
	
	public AccountRepository()
	{
		super(DBPool.ACCOUNT_DATABASE);
	}
	
	@Override
	public int createAccount(String email, String passwordHash)
	{
		Enclosure<Integer> accountId = new Enclosure<>(-1);
		
		try
		{
			executeInsert(CREATE_ACCOUNT, result ->
			{
				if (result.next())
				{
					accountId.setEnclosed(result.getInt(1));
				}
			}, email, passwordHash, AccessLevel.LOGGED_IN.name());
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
		}
		
		return accountId.getEnclosed();
	}

	@Override
	public IUserAccount selectAccount(int id)
	{
		Enclosure<IUserAccount> account = new Enclosure<>();
		
		try
		{
			executeInsert(SELECT_ACCOUNT_ID, result ->
			{
				if (result.next())
				{
					int accountId = result.getInt(1);
					String emailAddress = result.getString(2);
					String passwordHash = result.getString(3);
					AccessLevel accessLevel = AccessLevel.valueOf(result.getString(4));
					account.setEnclosed(new UserAccount(accountId, emailAddress, passwordHash, accessLevel));
				}
			}, id);
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
		}
		
		return account.getEnclosed();
	}

	@Override
	public IUserAccount selectAccount(String email)
	{
		Enclosure<IUserAccount> account = new Enclosure<>();

		try
		{
			executeInsert(SELECT_ACCOUNT_EMAIL, result ->
			{
				if (result.next())
				{
					int accountId = result.getInt(1);
					String emailAddress = result.getString(2);
					String passwordHash = result.getString(3);
					AccessLevel accessLevel = AccessLevel.valueOf(result.getString(4));
					account.setEnclosed(new UserAccount(accountId, emailAddress, passwordHash, accessLevel));
				}
			}, email);
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
		}

		return account.getEnclosed();
	}

	@Override
	public void updateAccountEmail(int id, String newEmail)
	{
		try
		{
			executeUpdate(UPDATE_ACCOUNT_EMAIL, newEmail, id);
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
		}
	}

	@Override
	public void updateAccountPassword(int id, String newPassword)
	{
		try
		{
			executeUpdate(UPDATE_ACCOUNT_EMAIL, newPassword, id);
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
		}
	}
}