package com.baselib.bean.metadata;

import java.lang.annotation.Annotation;

/**
 * 
 * @Auhtor: Mustafa   
 */

public class MetaTypeData <T extends Annotation> {

	private T type;
	private Class<?> typeClass;

	public MetaTypeData(T metaType,Class<?> typeClass) {
	
		this.type = metaType;
		this.typeClass = typeClass;
	}

	/**
	 * @return the type
	 */
	public T getType() {
		
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(T type) {
		
		this.type = type;
	}

	/**
	 * @return the typeClass
	 */
	public Class<?> getTypeClass() {
		
		return typeClass;
	}

	/**
	 * @param typeClass the typeClass to set
	 */
	public void setTypeClass(Class<?> typeClass) {
		
		this.typeClass = typeClass;
	}

}