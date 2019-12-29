package com.baselib.bean.metadata;

import java.lang.reflect.Method;

public class MethodMetaData <T> {
	
	Method method;
	
	T annotation;

	/**
	 * @return the annotation
	 */
	public T getAnnotation() {
		return annotation;
	}

	/**
	 * @param annotation the annotation to set
	 */
	public void setAnnotation(T annotation) {
		this.annotation = annotation;
	}

	/**
	 * @return the method
	 */
	public Method getMethod() {
		return method;
	}

	/**
	 * @param method the method to set
	 */
	public void setMethod(Method method) {
		this.method = method;
	}

}
