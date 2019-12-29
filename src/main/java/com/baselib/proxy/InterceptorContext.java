package com.baselib.proxy;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class InterceptorContext {
	
	Method method;
	
	Object[] args;
	
	Throwable exception;
	
	Object proxy;

	Object target;
	
	Object invokeResultValue;
	
	Object beforeInvokeResult;
	
	Map<String,Object> contextData;

	public InterceptorContext (Method method, Object[] args, Throwable exception,Object proxy,Object target) {

			this.method = method;
	
			this.args = args;
		
			this.exception = exception;
		
			this.proxy = proxy ;
		
			this.target = target;
		
			this.contextData = new HashMap<String,Object>();
	}
	
	/**
	 * @return the contextData
	 */
	public Object getContextData(String key) {
		return contextData.get(key);
	}
	/**
	 * @return the contextData
	 */
	public void setContextData(String key,Object value) {
		 contextData.put(key, value);
	}

	/**
	 * @return the proxy
	 */
	public Object getProxy() {
		return proxy;
	}

	/**
	 * @return the target
	 */
	public Object getTarget() {
		return target;
	}

	
	/**
	 * @return the args
	 */
	public Object[] getArgs() {
		
		return args;
	}


	/**
	 * @return the exception
	 */
	public Throwable getException() {
		
		return exception;
	}


	/**
	 * @return the method
	 */
	public Method getMethod() {
		
		return method;
	}


	/**
	 * @param exception the exception to set
	 */
	public void setException(Throwable exception) {
		this.exception = exception;
	}

	/**
	 * @return the invokeResultValue
	 */
	public Object getInvokeResultValue() {
		return invokeResultValue;
	}

	/**
	 * @param invokeResultValue the invokeResultValue to set
	 */
	public void setInvokeResultValue(Object invokeResultValue) {
		this.invokeResultValue = invokeResultValue;
	}

	/**
	 * @return the beforeInvokeResult
	 */
	public Object getBeforeInvokeResult() {
		return beforeInvokeResult;
	}

	/**
	 * @param beforeInvokeResult the beforeInvokeResult to set
	 */
	public void setBeforeInvokeResult(Object beforeInvokeResult) {
		this.beforeInvokeResult = beforeInvokeResult;
	}

}
