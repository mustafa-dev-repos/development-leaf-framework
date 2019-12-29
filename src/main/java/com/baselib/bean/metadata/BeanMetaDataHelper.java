package com.baselib.bean.metadata;

import java.util.HashMap;
import java.util.Map;

import com.baselib.util.ClassUtil;

/**
 * 
 * @Auhtor: Mustafa   
 */

public class BeanMetaDataHelper {
	
	private static Map<String, BeanMetaData> objectCache = new HashMap<String, BeanMetaData>();

	public static BeanMetaData getClassDescriptor(String implClassName) throws ClassNotFoundException {
		return getClassDescriptor(ClassUtil.getClass(implClassName));
	}

	public static BeanMetaData getClassDescriptor(Class<?> implClass) {

		BeanMetaData implObject = objectCache.get( implClass.getName() );
		
		if ( implObject != null) {
		
			return implObject; 
		} else {
			
			return createDescriptor( implClass );
		}
	}
	
	private static synchronized BeanMetaData createDescriptor(Class<?> implClass) {

		BeanMetaData object = objectCache.get(implClass.getName());

		if (object != null) {
			
			return object;
			
		} else {
			
			BeanMetaData singletonObject = new BeanMetaData(implClass);

			objectCache.put(implClass.getName() , singletonObject);
				
			return singletonObject;
		}
	}
}
