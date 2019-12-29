package com.leaf.framework.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 
 * @Auhtor: Mustafa   
 */

public class LoggingProxy implements InvocationHandler {

	protected Object delegate;

	public LoggingProxy(Object delegate) {
		this.delegate = delegate;
	}

	public Object invoke(Object proxy, Method method, Object[] args)
		throws Throwable {

		try {

			System.out.println(
				"Calling method "
					+ method
					+ " at "
					+ System.currentTimeMillis());
			Object result = method.invoke(delegate, args);
			return result;
		} catch (InvocationTargetException e) {
			throw e.getTargetException();
		} finally {

			System.out.println(
				"Called method("
					+ method
					+ " at "
					+ System.currentTimeMillis());
		}

	}

}
