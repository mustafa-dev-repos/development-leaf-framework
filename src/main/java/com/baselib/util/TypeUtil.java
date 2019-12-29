package com.baselib.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 
 * @Auhtor: Mustafa   
 */

public class TypeUtil {

	private TypeUtil() {
		super();
	}

	public static Class getGenericParameterTypes(Method method) {

		Type[] genericParameterTypes = method.getGenericParameterTypes();

		for(Type genericParameterType : genericParameterTypes){
		    if(genericParameterType instanceof ParameterizedType){
		        ParameterizedType aType = (ParameterizedType) genericParameterType;
		        Type[] parameterArgTypes = aType.getActualTypeArguments();
		        for(Type parameterArgType : parameterArgTypes){
		            Class parameterArgClass = (Class) parameterArgType;
		            return parameterArgClass;

		        }
		    }
		}
		return null;
	}

	public static  void getGenericType(Field field) {
		Type genericFieldType = field.getGenericType();
		    
		if(genericFieldType instanceof ParameterizedType){
		    ParameterizedType aType = (ParameterizedType) genericFieldType;
		    Type[] fieldArgTypes = aType.getActualTypeArguments();
		    for(Type fieldArgType : fieldArgTypes){
		        Class fieldArgClass = (Class) fieldArgType;
		        System.out.println("fieldArgClass = " + fieldArgClass);
		    }
		}
	}

	public static Type[] getReturnTypeArguments(Method method) {
		
		Type returnType = method.getGenericReturnType();

		if(returnType instanceof ParameterizedType) {
		
		    ParameterizedType type = (ParameterizedType) returnType;

		    return type.getActualTypeArguments();
		
		}

		return null;
	}


}
