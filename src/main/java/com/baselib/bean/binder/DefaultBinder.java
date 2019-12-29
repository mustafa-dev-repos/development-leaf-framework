package com.baselib.bean.binder;


/**
 * 
 * 
 * @Auhtor: Mustafa 
 */
 
public class DefaultBinder implements Binder {

	
	public DefaultBindTo bind(Class<?> clazz) {
		return new DefaultBindTo(clazz,this);
	}
	

	
	
}
