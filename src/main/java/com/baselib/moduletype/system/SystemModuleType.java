package com.baselib.moduletype.system;

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

@MetaAnnotationType(handler = SystemModuleTypeProcessor.class, order = MetaTypeOrder.SYSTEM_MODULE_TYPE_ORDER)

/**
 * 
 * 
 * @Auhtor: Mustafa 
 */

public @interface SystemModuleType {
	  int order() default 1000; 

}
