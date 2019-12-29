package com.baselib.watchdog;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.baselib.bean.metatype.MetaAnnotationType;
import com.baselib.bean.metatype.MetaTypeOrder;

@Target( {ElementType.TYPE} )
@Retention( RetentionPolicy.RUNTIME )
@Documented

@MetaAnnotationType(handler = WatchDogTypeProcessor.class, order = MetaTypeOrder.MODULE_TYPE_ORDER)

/**
 * 
 * 
 * @Auhtor: Mustafa 
 */

public @interface WatchDogType {
	  
	public String name();

	public long refreshRateInMs();
	
	public int order() default 1000;
	  
}
