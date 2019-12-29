package com.baselib.cache;

/**
 * 
 * @Auhtor: Mustafa 
 */

public class CachedObject
{
	private Object cachedObject;

	private long expirationDate = 0L;

	protected CachedObject(Object cachedObject, long expirationTime)
	{
		
		super();
		
		this.cachedObject = cachedObject;
		
		if (expirationTime == CacheManager.EXPIRATION_TIME_NEVER)
		{
			this.expirationDate = expirationTime;
		}
		else
		{
			this.expirationDate = System.currentTimeMillis() + expirationTime;
		}
	}

	public Object getCachedObject()
	{
		return cachedObject;
	}

	public long getExpirationDate()
	{
		return expirationDate;
	}

	public boolean isExpired()
	{
		return System.currentTimeMillis() > expirationDate;
	}

}
