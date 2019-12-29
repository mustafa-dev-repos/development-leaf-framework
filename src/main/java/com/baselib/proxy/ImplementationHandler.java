package com.baselib.proxy;

import java.lang.reflect.InvocationHandler;

//targetless interceptor..

/**
 * 
 * @Auhtor: Mustafa   
 */

//TODO refactor no need for implementation handler .. 
public interface ImplementationHandler extends InvocationHandler {

	public void initialize(Class<?> interfaceClass);
	
}
