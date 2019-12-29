package com.baselib.bean.metatype;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( {ElementType.ANNOTATION_TYPE} )
@Retention( RetentionPolicy.RUNTIME )
@Documented

/**
 * MetaMetaMeta layer information
 * 
 * @Auhtor: Mustafa 
 */

public @interface MetaAnnotationType {
 
		Class<? extends MetaAnnotationProcessor> handler() ;
		int order() default 1000;
}
