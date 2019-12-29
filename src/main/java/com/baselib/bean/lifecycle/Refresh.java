package com.baselib.bean.lifecycle;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( {ElementType.METHOD} )
@Retention( RetentionPolicy.RUNTIME )
@Documented

/**
 * 
 * 
 * @Auhtor: Mustafa 
 */

public @interface Refresh {
	
	Class<? extends RefreshInterceptor> refreshInterceptor() default DefaultRefreshInterceptor.class;

	long refreshRateInMs();
	
}
