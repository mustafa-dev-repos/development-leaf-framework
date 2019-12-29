package com.leaf.framework.proxy;

import java.lang.reflect.Method;

import com.baselib.proxy.ImplementationHandler;

/**
 * 
 * @Auhtor: Mustafa   
 */

public abstract class BaseImplementationHandler implements ImplementationHandler {

	public void initialize(Class<?> interfaceClass) {

	}

	public abstract Object invoke(Object proxy, Method method, Object[] args) throws Throwable;
}
