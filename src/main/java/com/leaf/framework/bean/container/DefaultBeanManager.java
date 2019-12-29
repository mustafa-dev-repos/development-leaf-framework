package com.leaf.framework.bean.container;

import java.text.MessageFormat;

import com.baselib.bean.binder.BindingHelper;
import com.baselib.bean.container.BeanHelper;
import com.baselib.bean.container.BeanManager;
import com.baselib.bean.container.ProvidedByHelper;
import com.baselib.bean.container.SingletonHelper;
import com.baselib.bean.cyclic.BeanCreationInfo;
import com.baselib.bean.cyclic.CyclicHelper;
import com.baselib.bean.lifecycle.LifeCycleHelper;
import com.baselib.bean.metadata.BeanMetaData;
import com.baselib.bean.metadata.BeanMetaDataHelper;
import com.baselib.cache.Cache;
import com.baselib.exception.TechnicalException;
import com.baselib.proxy.CacheInterceptorHandler;
import com.baselib.proxy.InterceptorBuilder;
import com.baselib.proxy.InterceptorHandler;
import com.baselib.proxy.InvocationInterceptor;
import com.baselib.proxy.ProxyFactory;
import com.baselib.util.ClassUtil;
import com.leaf.framework.dab.annotation.SQLCallerInterface;
import com.leaf.framework.dab.proxy.ProcedureCallProxy;
import com.leaf.framework.transaction.annotation.Transactional;
import com.leaf.framework.transaction.interceptor.TransactionInterceptor;

/**
 * 
 * @Auhtor: Mustafa 
 */

public class DefaultBeanManager implements BeanManager {
	
	private String CLASS_NAME = DefaultBeanManager.class.getName(); 

	private static final String ERROR_IMPL_CLASS_NOT_FOUND = "Implementation class definition not found for [{0}] interface";

	public DefaultBeanManager() {

	}

	@SuppressWarnings("unchecked")
	public <T> T getBean(Class<T> clazz) {

		T instance = null;

		BeanMetaData interfaceDesc = BeanMetaDataHelper.getClassDescriptor( clazz );

		BeanCreationInfo beanCreationInfo = CyclicHelper.getBeanCreationInfo();
		
		beanCreationInfo.checkWaitingForInstance(clazz.getName());
		
		try {
	
			beanCreationInfo.setWaitingInstanceFor(clazz.getName());
			
			instance = getPureInstance(clazz, interfaceDesc);
		}
		finally {
			
		    beanCreationInfo.releaseWaitingInstanceFor(clazz.getName());
		}
		
		if (interfaceDesc.isInitializeEnabled() &&
		   !interfaceDesc.isInitialized() ) {

			LifeCycleHelper.initialize(instance, interfaceDesc);
		}

		InterceptorBuilder interceptorBuilder = new InterceptorBuilder();
		
		if (interfaceDesc.isUsingAnnotation(Cache.class)) {

			InterceptorHandler interceptorHandler = BeanHelper.getBean( CacheInterceptorHandler.class);
			interceptorBuilder.addInterceptorHandler(interceptorHandler);
		}

		if (interfaceDesc.isUsingAnnotation(Transactional.class)) {

			InterceptorHandler interceptorHandler = BeanHelper.getBean( TransactionInterceptor.class);
			interceptorBuilder.addInterceptorHandler(interceptorHandler);
		}
		
		if (interceptorBuilder.getInterceptorChain() != null ) {

			InvocationInterceptor invocInterceptor = BeanHelper.getBean( InvocationInterceptor.class );
			
			invocInterceptor.setTarget(instance);
			invocInterceptor.addInterceptorHandler(interceptorBuilder.getInterceptorChain());
			
			return ProxyFactory.createInterceptorProxy(clazz, instance,	invocInterceptor);
			
		} 

		return instance;
	}

	@SuppressWarnings("unchecked")
	private <T> T getPureInstance(Class<T> clazz,BeanMetaData classDesc) {

		T instance = null;

		if (classDesc.isSingleton() ) {

			instance = (T) SingletonHelper.getObject(clazz);  
			
			if (instance != null)
				return instance;
		}
			
		if (classDesc.isInterface() || classDesc.isAbstract()) {
			
			// create implementation proxy and return..
			if (classDesc.isUsingAnnotation(SQLCallerInterface.class)) {

				if (classDesc.isSingleton() ) {

					return (T) SingletonHelper.createSingletonObjectFor(classDesc,ProcedureCallProxy.class);
				}
				else {
					
					return ProxyFactory.createImplementationProxy(clazz, ProcedureCallProxy.class);
				}
			}

			Class<T> implClass = null;

			implClass = BindingHelper.get( clazz );

			if (implClass != null ) {
			
				if (classDesc.isSingleton() ) {

					return (T) SingletonHelper.createSingletonObject(classDesc, implClass);
				}
				else {
					return ClassUtil.newInstance(implClass);
				}
			}
			
			if (classDesc.isImplementedBy()) {
				
				implClass = classDesc.getImplementedByClass();

					if (classDesc.isSingleton() ) {
						
						return (T) SingletonHelper.createSingletonObject(classDesc, implClass);
					}
					else {
						return ClassUtil.newInstance(implClass);
					}
			}

			if (classDesc.isProvidedBy()) {
				Class providedImplClass = ProvidedByHelper.getImplClass(clazz,classDesc);
				
				if (classDesc.isSingleton() ) {
					return (T) SingletonHelper.createSingletonObject(classDesc, providedImplClass);
				}
				else {
					return (T) ClassUtil.newInstance(providedImplClass);
				}
			}
				
				String message = MessageFormat.format(ERROR_IMPL_CLASS_NOT_FOUND, new Object[]{clazz.getName() });
				throw new TechnicalException(CLASS_NAME, message);

		} 
		else {

			instance = (T) ClassUtil.newInstance(clazz);
			return instance;
		}
	}
	
	// TODO CHECK warnings
	public Object getBean(String beanName) {

		if (ClassUtil.isPresent(beanName)) {

			return (Object) getBean(ClassUtil.forName(beanName));
		}

		// TODO : make parametrik this message..
		throw new TechnicalException("No class definition found  for " + beanName);

	}

}
