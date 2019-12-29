package com.baselib.cache;

import java.text.MessageFormat;

import com.baselib.bean.container.BeanHelper;
import com.baselib.log.LogHelper;
import com.baselib.proxy.InterceptorContext;
import com.baselib.proxy.InterceptorHandler;

public class DefaultCacheInterceptorHandler implements InterceptorHandler {
	
    private static final String DEBUG_CACHE_HIT_INFO = "Data for the [{0}] key found in cache";

    private static final String DEBUG_DATA_ADDED_TO_CACHE = "Data for the [{0}] key added into cache";
    
	private static final String CLASS_NAME = DefaultCacheInterceptorHandler.class.getName();

	private static String CACHE_KEY = CLASS_NAME + ".key";
	
	private CacheManager cacheManager = BeanHelper.getBean(CacheManager.class);

	protected static LogHelper log = LogHelper.getLog();

public final void beforeInvoke(InterceptorContext interceptorContext) throws Throwable {
	
	String key = cacheManager.createKeyForCache(interceptorContext.getMethod().getDeclaringClass().getName(),interceptorContext.getMethod(),interceptorContext.getArgs());
	
	Object obj = cacheManager.getElement(key);
	
	//return result from cache
	if (obj != null) {
		
		interceptorContext.setBeforeInvokeResult(obj);

		if (log.isDebugEnabled()) {
			
			String message = MessageFormat.format(DEBUG_CACHE_HIT_INFO, new Object[]{key });
			log.debug(message);	
		}
		
	}
	else
	{  
		// keep cache key to store in cache..
		interceptorContext.setContextData(CACHE_KEY,key);
	}
}

public final void afterInvoke(InterceptorContext interceptorContext)  throws Throwable {
	
	// if not found in cache and has invoke result then put result into cache for later use
	if (interceptorContext.getBeforeInvokeResult() == null &&
		interceptorContext.getInvokeResultValue() != null ) {
		
		Cache cache = interceptorContext.getMethod().getAnnotation(Cache.class);
		
		if (cache == null) {
			
			cache = interceptorContext.getMethod().getDeclaringClass().getAnnotation(Cache.class);
		}
		
		if (cache == null) {
			
			//TODO log.debug   technical exceptino occured..
		}

		long time = 0 ;
		
		if (cache.expirationTimeInSecs() == 0 &&
			cache.expirationTimeInMinutes() == 0 && 
			cache.expirationTimeInHours() == 0 ) {

			time = Long.MAX_VALUE;
		}
		else {
			
			time = cache.expirationTimeInSecs() + 
				  (cache.expirationTimeInMinutes() * 60) + 
				  (cache.expirationTimeInHours() * 60 * 60);

			time = time * 1000;
		}
		
		cacheManager.addElement((String) interceptorContext.getContextData(CACHE_KEY), interceptorContext.getInvokeResultValue(), time);
		
		if (log.isDebugEnabled()) {

			String message = MessageFormat.format(DEBUG_DATA_ADDED_TO_CACHE, new Object[]{ interceptorContext.getContextData(CACHE_KEY) });
			log.debug(message);	
		}
		
	}
}

public final void afterThrowing(InterceptorContext interceptorContext) throws Throwable {
	
}

public final void afterFinally(InterceptorContext interceptorContext) {

}

}
