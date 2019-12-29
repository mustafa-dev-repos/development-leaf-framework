package com.leaf.framework.transaction.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target( {ElementType.TYPE,ElementType.METHOD} )
@Retention( RetentionPolicy.RUNTIME )
@Documented

/**
 * 
 * @Auhtor: Mustafa   
 */

public @interface Transactional {

	/**
	 *  If this annotation used in class definition 
	 *  <strong>requiresNew</strong> means all methods requires a new transaction. 
	 * 	
	 * 	If this annotation used in method <strong>requiresNew</strong>  
	 *  means that method requires a new transaction.
	 *  @default default value is false.
	 */
    boolean requiresNew() default false;	
}
