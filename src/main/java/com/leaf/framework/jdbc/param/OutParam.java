package com.leaf.framework.jdbc.param;

/**
 * 
 * @Auhtor: Mustafa   
 */

public class OutParam extends Param {

	private static final long serialVersionUID = -3014416642792118597L;

	public OutParam(int type, int paramIndex) {

		super(type, paramIndex);
	}

	public OutParam(int type, int paramIndex, String typeName) {

		super(type, paramIndex, typeName);
	}
}
