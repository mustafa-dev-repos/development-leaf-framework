package com.baselib.model;

import java.io.Serializable;

public class NameValuePair implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8414929311784915132L;

	private String name;
	private Object value;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}

}
