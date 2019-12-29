package com.baselib.config.system;


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

@MetaAnnotationType(handler = SystemConfigTypeProcessor.class, order = MetaTypeOrder.SYSTEM_CONFIG_TYPE_ORDER )

/**
 * 
 * 
 * @Auhtor: Mustafa 
 */

public @interface SystemConfigType {

	int order() default 1000; 

}
