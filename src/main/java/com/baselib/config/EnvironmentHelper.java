package com.baselib.config;

import com.baselib.util.ClassUtil;

public class EnvironmentHelper {
	
	
	private static final String WEB_APPLICATION = "javax.servlet.ServletContext";
	
	private static Boolean isWebApplication  ;
	
	private static final String EHCACHE_1_5 = "net.sf.ehcache.Ehcache";

	public static boolean isEhCachePresent() {
		
		return ClassUtil.isPresent(EHCACHE_1_5); 
	}

	public static boolean isWebApplication() {
		
		if (isWebApplication  == null) {

			synchronized (isWebApplication) {

				if (isWebApplication != null) {
					return isWebApplication.booleanValue();
				}

				isWebApplication = new Boolean( ClassUtil.isPresent(WEB_APPLICATION));
			} 
		}
		return isWebApplication.booleanValue(); 
	}


	public static boolean isSpringPresent() {
		
		return false; 
	}

	
}
