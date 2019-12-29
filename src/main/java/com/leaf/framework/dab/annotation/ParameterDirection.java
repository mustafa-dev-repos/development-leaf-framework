package com.leaf.framework.dab.annotation;

/**
 * 
 * @Auhtor: Mustafa   
 */

public enum ParameterDirection {

    /**
     * <<DEFAULT>> 
     * This is default value if value is not specified
     *  
     */

	IN,

    /**
     * Output parameter
     */

	OUT,

    /**
     * Input and output parameter
     */

	IN_OUT,

    /**
     * parameter will not be passed to stored procedure. 
     */

	VOID,
    
    /**
     * Output cursor
     */

	OUT_CURSOR
	
}
