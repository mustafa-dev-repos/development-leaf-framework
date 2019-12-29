package com.leaf.framework.jdbc.param;

import java.io.Serializable;

/**
 * 
 * @Auhtor: Mustafa   
 */

public class NullParam implements Serializable {

	private static final long serialVersionUID = 3180851196490488612L;
	
	private int jdbcType;

    public NullParam(int type) {
        jdbcType = type;
    }

    public int getType() {
        return jdbcType;
    }

    public void setType(int type) {
        jdbcType = type;
    }
    
    public String toString() { 
    	return "NULL"; 
	} 

}
