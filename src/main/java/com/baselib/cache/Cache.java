package com.baselib.cache;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented

/**
 * 
 * @Auhtor: Mustafa 
 */

public @interface Cache {

   long expirationTimeInHours() default 0;
   long expirationTimeInMinutes() default 0;
   long expirationTimeInSecs() default 0;
   
  // long expirationTime() default -1;
}
