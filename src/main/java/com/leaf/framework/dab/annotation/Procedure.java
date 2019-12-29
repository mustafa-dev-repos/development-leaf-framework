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

import com.leaf.framework.dab.call.ProcedureType;
import com.leaf.framework.dab.converter.DefaultResultConverter;
import com.leaf.framework.dab.converter.ResultConverter;
import com.leaf.framework.dab.interceptor.DefaultSQLInterceptor;
import com.leaf.framework.dab.interceptor.SQLInterceptor;

@Target( ElementType.METHOD )
@Retention( RetentionPolicy.RUNTIME )
@Documented

public @interface Procedure {

	  
	SQLParamOut[] outParams() default @SQLParamOut(paramIndex = -1, returnType = void.class );

	/**
     * <<Optional>>
     * Stored procedure name. If null stored procedure name is same with method name.   
     * 
     */

	String name() default "";

	/**
     * <<Optional>>
     * Package name for the called procedure. 
     * 
     */
	
	String prefix() default "" ;

	String jndiName() default "" ;

	ProcedureType procedureType() default ProcedureType.PROCEDURE ;

	//TODO: handle this property in release 2
	Class<? extends ResultConverter> resultConverter() default DefaultResultConverter.class ;

	Class<? extends SQLInterceptor> invocationInterceptor() default DefaultSQLInterceptor.class ;
	
  }
