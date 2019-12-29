package com.baselib.bean;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.baselib.module.DefaultModule;
import com.baselib.moduletype.Module;


@Target( {ElementType.TYPE} )
@Retention( RetentionPolicy.RUNTIME )
@Documented

/**
 * 
 * @Auhtor: Mustafa 
 * 
 */

public @interface Bean {

	public Class<? extends Module> module() default DefaultModule.class; 

	public int order() default 1000;

	public boolean singleton() default true;
	
	public Class<?> implementedBy() default DefaultImplementedBy.class;

	public String implementedByRemote() default "";
	
	public Class<? extends ImplementationProvider> providedBy() default DefaultProvidedBy.class;

	public String providedByRemote() default "";
	
}
