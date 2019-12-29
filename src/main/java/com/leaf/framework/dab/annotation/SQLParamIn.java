package com.leaf.framework.dab.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( {ElementType.PARAMETER } )
@Retention( RetentionPolicy.RUNTIME )
@Documented

public @interface SQLParamIn {

    /**
     * <<Optional>>
     * Stored procedure parameter name.
     */
	String name() default "";

    /**
     * <<Required>>
     * if parameter direction is out , return type of parameter 
     * 
     */
	// TODO : remove this option and use parameter type as return type...
	Class returnType()  ;

}
