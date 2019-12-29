package com.leaf.framework.dab.annotation;

/**
 * 
 * @Auhtor: Mustafa   
 */

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( {ElementType.ANNOTATION_TYPE } )
@Retention( RetentionPolicy.RUNTIME )
@Documented

public @interface SQLParamOut {

    /**
     * <<Optional>>
     * Stored procedure parameter name.
     */

	String name() default "" ;

    /**
     * <<Required>>
     * Stored procedure parameter name.
     */

	int paramIndex() ;
	
    /**
     * <<Required>>
     * if parameter direction is out , return type of parameter 
     * 
     */

	Class returnType()  ;

    /**
     * <<Optional>>
     * This may be required in the case of ARRAY or STRUCT types etc. 
     * 
     */
    String jdbcTypeName() default "" ;
}
