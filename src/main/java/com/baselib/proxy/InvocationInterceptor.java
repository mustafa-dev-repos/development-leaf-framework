package com.baselib.proxy;

import java.lang.reflect.InvocationHandler;

import com.baselib.bean.Bean;
import com.baselib.module.system.SystemDefaultModule;

/**
 *
 * 
 * @Auhtor: Mustafa   
 */

@Bean(implementedBy = DefaultInvocationInterceptor.class, module = SystemDefaultModule.class, singleton = false)

public interface InvocationInterceptor extends InvocationHandler {

	public void setTarget(Object target);

	public void addInterceptorHandler(InterceptorChain interceptorChain);

	public InterceptorHandler getInterceptor();
	
	public Object getTarget();
	
}
