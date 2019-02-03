package org.hacksmu.pickmeup.api.webserver;

import java.util.concurrent.TimeUnit;

import org.hacksmu.pickmeup.api.account.AccessLevel;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public abstract class CacheRoute<T> extends BasicRoute
{
	private final Cache<String, T> _cache;

	public CacheRoute(AccessLevel level, long cacheDuration, TimeUnit cacheDurationUnit)
	{
		super(level);
		_cache = CacheBuilder.newBuilder().expireAfterWrite(cacheDuration, cacheDurationUnit).build();
	}

	protected void addToCache(String key, T value)
	{
		_cache.put(key.toUpperCase(), value);
	}

	protected T getFromCache(String key)
	{
		return _cache.getIfPresent(key);
	}

	public Cache<String, T> getCache()
	{
		return _cache;
	}
}