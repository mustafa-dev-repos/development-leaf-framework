package com.baselib.proxy;

import java.lang.reflect.Method;


/**
 * 
 * @Auhtor: Mustafa   
 */

public class DefaultInvocationInterceptor implements InvocationInterceptor {
	
	private InterceptorChain interceptors;
	
	private Object target;

	public DefaultInvocationInterceptor() {
		
			super();
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		
		InterceptorContext context = new InterceptorContext (method, args, null,proxy,target);

		return interceptors.start(context);
	}

	public InterceptorHandler getInterceptor() {
		
		return null;
	}

	public Object getTarget() {

		return null;
	}

	public void setTarget(Object target) {
		
		this.target = target;
	}

	public void addInterceptorHandler(InterceptorChain interceptorChain) {

		this.interceptors = interceptorChain;
    }
	
}
