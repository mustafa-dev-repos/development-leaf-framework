package com.baselib.bean.binder;


/**
 * 
 * 
 * @Auhtor: Mustafa 
 */

public class DefaultBindTo {
	
	private Class<?> bindSource;
	private Binder  module;


	public DefaultBindTo (Class<?> clazz,Binder  module){
		bindSource = clazz;
		this.module = module;
		
	}
	
	public Binder to(Class<?> clazz){
		BindingHelper.put(bindSource, clazz);
		return module;
	}

}
