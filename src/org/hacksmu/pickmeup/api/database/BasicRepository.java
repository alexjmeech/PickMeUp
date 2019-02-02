package org.hacksmu.pickmeup.api.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

public abstract class BasicRepository
{
	private final DataSource _source;
	
	public BasicRepository(DataSource source)
	{
		_source = source;
	}
	
	protected Connection getConnection() throws SQLException
	{
		return _source.getConnection();
	}
	
	protected int executeUpdate(String sql, Object... values) throws SQLException
	{
		int affected = 0;
		try (Connection c = getConnection();
			PreparedStatement ps = c.prepareStatement(sql);
			)
		{
			for (int i = 0; i < values.length; i++)
			{
				ps.setObject(i + 1, values[i]);
			}
			affected = ps.executeUpdate();
		}
		
		return affected;
	}
	
	protected int executeInsert(String sql, ResultSetConsumer handler, Object... values) throws SQLException
	{
		int inserted = 0;
		try (Connection c = getConnection();
			PreparedStatement ps = c.prepareStatement(sql);
			)
		{
			for (int i = 0; i < values.length; i++)
			{
				ps.setObject(i + 1, values[i]);
			}
			inserted = ps.executeUpdate();
			
			if (handler != null)
			{
				handler.accept(ps.getGeneratedKeys());
			}
		}
		
		return inserted;
	}
	
	protected void executeQuery(String sql, ResultSetConsumer handler, Object... values) throws SQLException
	{
		try (Connection c = getConnection();
			PreparedStatement ps = c.prepareStatement(sql);
			)
		{
			for (int i = 0; i < values.length; i++)
			{
				ps.setObject(i + 1, values[i]);
			}
			ResultSet resultSet = ps.executeQuery();

			if (handler != null)
			{
				handler.accept(resultSet);
			}
		}
	}
}