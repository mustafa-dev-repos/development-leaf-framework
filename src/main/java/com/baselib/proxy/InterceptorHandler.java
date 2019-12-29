package com.baselib.proxy;

public interface InterceptorHandler {
	
    public void beforeInvoke(InterceptorContext interceptorContext) throws Throwable ;

    public void afterInvoke(InterceptorContext interceptorContext) throws Throwable;

    public void afterThrowing(InterceptorContext interceptorContext) throws Throwable ;

    public void afterFinally(InterceptorContext interceptorContext);

  
}


