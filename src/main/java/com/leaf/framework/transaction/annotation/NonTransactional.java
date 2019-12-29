package com.leaf.framework.transaction.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @Auhtor: Mustafa   
 */

@Target( {ElementType.METHOD} )
@Retention( RetentionPolicy.RUNTIME )
@Documented

public @interface NonTransactional {
	
    boolean requiresNew() default false;	
}
