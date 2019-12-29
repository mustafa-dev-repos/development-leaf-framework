package com.baselib.proxy;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import com.baselib.bean.container.BeanHelper;
import com.baselib.bean.metadata.BeanMetaData;
import com.baselib.bean.metadata.BeanMetaDataHelper;
import com.baselib.util.ClassUtil;

/**
 * 
 * @Auhtor: Mustafa   
 */

public class ProxyFactory {

	// proxy object cache must be a separate cache from bean object cache..
	private static Map<Class, Object> proxyObjectCache = new HashMap<Class, Object>();

	@SuppressWarnings("unchecked")
	public static <T> T createInterceptorProxy(Class<T> interfaceClass,T target, InvocationInterceptor invocInterceptor) {

		return (T)Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[] {interfaceClass}, invocInterceptor);
	}

	

/*	public static <T> T createInterceptorProxy(Class<T> interfaceClass,T target, Class<? extends InterceptorHandler>  interceptingHandlerInterfaceClass) {

		InterceptorHandler interceptorHandler = BeanHelper.getBean( interceptingHandlerInterfaceClass);

		InvocationInterceptor invocInterceptor = BeanHelper.getBean( InvocationInterceptor.class );

	//	invocInterceptor.addInterceptor(target, interceptorHandler);

		return (T)Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[] {interfaceClass}, invocInterceptor);
	}
*/
	@SuppressWarnings("unchecked")
	public static <T> T createImplementationProxy(Class<T> interfaceClass, Class<? extends ImplementationHandler>  implementationHandlerClass) {

		BeanMetaData metaData = BeanMetaDataHelper.getClassDescriptor(interfaceClass);
		
		if (metaData.isSingleton()) {

			Object proxy = proxyObjectCache.get(interfaceClass);
		
			if (proxy != null)
				return (T) proxy;
			else
				return createCachedImplHandlerProxy(interfaceClass, implementationHandlerClass);
		}
		else {
			return createImplHandlerProxy(interfaceClass, implementationHandlerClass);
		}
	}
	
	@SuppressWarnings("unchecked")
	private synchronized static <T> T createCachedImplHandlerProxy(Class<T> interfaceClass, Class<? extends ImplementationHandler> invocHandler) {

		Object proxy = proxyObjectCache.get(interfaceClass);

		if (proxy != null) {
			
			return (T) proxy;
		} else {
			
			Object proxyObject = createImplHandlerProxy(interfaceClass, invocHandler);

			proxyObjectCache.put(interfaceClass, proxyObject);
				
			return (T) proxyObject;
		}
	}
	
	@SuppressWarnings("unchecked")
	private static <T> T createImplHandlerProxy(Class<T> interfaceClass, Class<? extends ImplementationHandler> invocHandler) {

		ImplementationHandler handler = null; 
			
		if (invocHandler.isInterface()) {
			// get implemetation object from interface..
			handler = BeanHelper.getBean(invocHandler);
		}
		else {
			handler = ClassUtil.newInstance( invocHandler );
		}

			handler.initialize(interfaceClass);

		return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[] { interfaceClass }, handler);

	}
}
