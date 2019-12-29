package com.leaf.framework.jdbc.persistent.callable;


/**
 * 
 * @Auhtor: Mustafa   
**/

public interface OutParamAware {

	public void registerAsOutParam(int typeofOutParameter);
	public void registerAsOutParam(int typeofOutParameter, String typeName);

}
