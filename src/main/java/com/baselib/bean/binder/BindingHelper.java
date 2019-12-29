package com.baselib.bean.binder;

import java.util.HashMap;
import java.util.Map;

import com.baselib.bean.container.BeanHelper;

/**
 * 
 * 
 * @Auhtor: Mustafa 
 */

public class BindingHelper {

	private static Map<Class,Class>  bindMap = new HashMap<Class,Class>() ;

	private static Binder binder = BeanHelper.getBean(Binder.class); 
	
	protected static void put(Class interfaceClass, Class clazz2) {
		
		bindMap.put( interfaceClass, clazz2);
	}

	public static Class get(Class  interfaceClass) {
		
		return bindMap.get( interfaceClass );
	}

	/**
	 * @return the binder
	 */
	public static Binder getBinder() {
		return binder;
	}

}
