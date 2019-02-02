package org.hacksmu.pickmeup.api.database;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

public final class DBPool
{
	private static final Map<String, DataSource> DATA_SOURCES = new HashMap<>();
	private static final ReentrantReadWriteLock MAP_LOCK = new ReentrantReadWriteLock();

	public static DataSource getDataSource(String databaseId)
	{
		MAP_LOCK.readLock().lock();
		if (DATA_SOURCES == null || DATA_SOURCES.isEmpty() || !DATA_SOURCES.containsKey(databaseId))
		{
			final int held = MAP_LOCK.getReadHoldCount();
			for (int i = 0; i < held; i++)
			{
				MAP_LOCK.readLock().unlock();
			}
			loadDataSources();
			for (int i = 0; i < held; i++)
			{
				MAP_LOCK.readLock().lock();
			}
		}
		
		DataSource source = DATA_SOURCES.get(databaseId);
		MAP_LOCK.readLock().unlock();
		return source;
	}

	private static DataSource openDataSource(String url, String username, String password)
	{
		BasicDataSource source = new BasicDataSource();
		source.addConnectionProperty("autoReconnect", "true");
		source.addConnectionProperty("allowMultiQueries", "true");
		source.setDefaultTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		source.setDriverClassName("com.mysql.jdbc.Driver");
		source.setUrl(url);
		source.setUsername(username);
		source.setPassword(password);
		source.setMaxTotal(3);
		source.setMaxIdle(3);
		source.setTimeBetweenEvictionRunsMillis(180 * 1000);
		source.setSoftMinEvictableIdleTimeMillis(180 * 1000);

		return source;
	}

	private static void loadDataSources()
	{
		MAP_LOCK.writeLock().lock();
		
		DATA_SOURCES.forEach((key, source) ->
		{
			try
			{
				((BasicDataSource)source).close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		});
		DATA_SOURCES.clear();
		try
		{
			File configFile = new File("database-config.dat");

			if (configFile.exists())
			{
				List<String> lines = Files.readAllLines(configFile.toPath(),
						Charset.defaultCharset());

				for (String line : lines)
				{
					if (line.startsWith("#"))
					{
						continue;
					}
					
					String[] args = line.split(" ");

					if (args.length == 4)
					{
						String dbId = args[0];
						String dbHost = args[1];
						String userName = args[2];
						String password = args[3];
						
						DATA_SOURCES.put(dbId, openDataSource("jdbc:mysql://" + dbHost, userName, password));
					}
				}
			}
			else
			{
				System.out.println("database-config.dat not found at "
						+ configFile.toPath().toString());
			}
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
			System.out.println("---Unable To Parse DBPOOL Configuration File---");
		}
		
		MAP_LOCK.writeLock().unlock();
	}
}