package com.leaf.framework.dab.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @Author Mustafa 
 *  
 */

// refactor this into beanhelper as singleton..
public class SQLInterceptorCacheManager {

	private static Map<Class, Object> objectCache = new HashMap<Class, Object>();

	public static Object getClassFromCache(Class clazz) {

		return objectCache.get(clazz);

	}
	
	public static void putClassToCache(Object object) {

		objectCache.put(object.getClass(), object);

	}

}
