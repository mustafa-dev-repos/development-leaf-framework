package com.leaf.framework.jdbc.param;

import java.io.Serializable;

/**
 * 
 * @Auhtor: Mustafa   
 */

public class Param  implements Serializable{

	private static final long serialVersionUID = -4593439997720584298L;

	private int paramIndex ;
	private int returnType;
	private String typeName;
	

	public Param(int type,int paramIndex) {

		this.returnType = type;
		this.paramIndex = paramIndex;

	}
	
	public Param(int type, int paramIndex,String typeName) {

		this.returnType = type;
		this.paramIndex = paramIndex;
		this.typeName = typeName;
	
	}

	/**
	 * @return the type
	 */
	public int getReturnType() {
		return returnType;
	}
	/**
	 * @return the typeName
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * @return the paramIndex
	 */
	public int getParamIndex() {
		return paramIndex;
	}
}
