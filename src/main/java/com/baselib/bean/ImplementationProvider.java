package com.baselib.bean;

/**
 * 
 * @Auhtor: Mustafa   
 */

public interface ImplementationProvider <T> {

	public Class<? extends T> getImplClass();
}
