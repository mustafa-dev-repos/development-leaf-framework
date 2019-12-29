package com.leaf.framework.dab.interceptor;
/**
 * Interface defining methods of a invocation interceptor that is called just before and right after stored procedure invocation.
 * @Author Mustafa 
public interface SQLInterceptor {
	public void beforeInvoke(DefaultProcedureContext ctx);
	/**
	public void afterInvoke(DefaultProcedureContext ctx);
}