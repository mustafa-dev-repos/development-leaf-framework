package com.leaf.framework.dab.proxy;

import java.util.HashMap;
import java.util.Map;


/**
 * 
 * @Auhtor: Mustafa   
 */
//TODO delete this class..
public class SQLProxyFactory {

	private static Map<Class, Object> proxyMap = new HashMap<Class, Object>();

	@SuppressWarnings("unchecked")
	public static <T> T getSQLProxy(Class<T> interfaceClass) {

		Object proxy = proxyMap.get(interfaceClass);
		
		if (proxy != null)
			return (T) proxy;
		else
			return createSQLProxy(interfaceClass);
	}

	@SuppressWarnings("unchecked")
	private synchronized static <T> T createSQLProxy(Class<T> interfaceClass) {

		// double checking works with java 5 ..
		Object proxy = proxyMap.get(interfaceClass);

		if (proxy != null) {
			return (T) proxy;
		} else {
			
		//	DefaultProcedureCallProxy handler = new DefaultProcedureCallProxy(interfaceClass);

			Object proxyObject =null;// (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[] { interfaceClass }, handler);

			proxyMap.put(interfaceClass, proxyObject);
			
			return (T) proxyObject;
		}

	}

}
