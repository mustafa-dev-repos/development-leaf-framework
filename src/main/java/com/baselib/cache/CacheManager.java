package com.baselib.cache;

import java.lang.reflect.Method;

import com.baselib.bean.Bean;
import com.baselib.watchdog.WatchDog;

@Bean (implementedBy= DefaultCacheManager.class,singleton = true)

/**
 *
 * 
 * @Auhtor: Mustafa 
 */

public interface CacheManager extends WatchDog {

	public static final long EXPIRATION_TIME_NEVER = Long.MAX_VALUE;

	public abstract void clearCache();

	public void addElement(String key, Object value);

	public void addElement(String key, Object value, long expirationTime);

	public Object getElement(String key);

	public String createKeyForCache(String interfaceClassName, Method method, Object[] methodArguments);

}
