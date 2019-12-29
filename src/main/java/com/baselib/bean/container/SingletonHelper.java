package com.baselib.bean.container;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.baselib.bean.metadata.BeanMetaData;
import com.baselib.proxy.ImplementationHandler;
import com.baselib.proxy.ProxyFactory;
import com.baselib.util.ClassUtil;

/**
 * 
 * 
 * @Auhtor: Mustafa 
 */

//TODO replace synhronized static with synchronized(block) ???

public class SingletonHelper {

	private static Map<String, Object> singletonObjectCache = new ConcurrentHashMap<String, Object>();
	
	public static void setObject(Class<?> interfaceClass, Object object) {

		singletonObjectCache.put(interfaceClass.getName(), object);
	}

	public static Object getObject(Class<?> interfaceClass) {

		return singletonObjectCache.get(interfaceClass.getName());
	}
	
	public static Object getObject(String interfaceClass) {

		return singletonObjectCache.get(interfaceClass);
	}

	public static Map<String, Object> getSingletonObjectList() {
	
		return singletonObjectCache;
	}
// TODO refactor this first get code , separate get and create functionalities.. 
	public static Object createSingletonObject(BeanMetaData interfaceDesc ,Class<?> implClass) {

		Object object = singletonObjectCache.get(interfaceDesc.getMetaDataClass().getName());

		if (object != null) {
			
			return object;
			
		} else {
			
			synchronized (interfaceDesc)  {
			
			object = singletonObjectCache.get(interfaceDesc.getMetaDataClass().getName());

			if (object != null) {
					
				return object;
			} 
			
			Object singletonObject = ClassUtil.newInstance( implClass );

			singletonObjectCache.put(interfaceDesc.getMetaDataClass().getName() , singletonObject);
				
			return singletonObject;

			}
		}
	}

	public static Object createSingletonObjectFor(BeanMetaData interfaceDesc,Class<? extends ImplementationHandler> proxyClass) {

		Object object = singletonObjectCache.get(interfaceDesc.getMetaDataClass().getName());

		if (object != null) {
			
			return object;
			
		} else {
			
			synchronized (interfaceDesc)  {

				object = singletonObjectCache.get(interfaceDesc.getMetaDataClass().getName());
			
			if (object != null) {
					
				return object;
			} 

				Object singletonObject = ProxyFactory.createImplementationProxy(interfaceDesc.getMetaDataClass(), proxyClass);

				singletonObjectCache.put(interfaceDesc.getMetaDataClass().getName() , singletonObject);
				
				return singletonObject;
			}
			}
	}

}
