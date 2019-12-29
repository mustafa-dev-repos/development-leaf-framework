package com.leaf.framework.jdbc.persistent.callable;

import java.util.List;

import com.leaf.framework.jdbc.param.ProcedureParamHolder;

/**
 * 
 * @Auhtor: Mustafa   
 */

public interface CallableParameterAware {

	public OutParamAware setNull(int type);

	public OutParamAware setParam(Object value); 

	public void setParams(List<Object> params);

	public void setParams(Object[] params);

	public OutParamAware setParam( int value);

	public OutParamAware setParam(long value); 

	public OutParamAware setParam( float value);

	public OutParamAware setParam(double value);
	
	public abstract void registerOutParam(final int typeofOutParameter) ;

	public abstract void registerOutParam(final int typeofOutParameter, String typeName);
	
	public void setParams(ProcedureParamHolder params);
}
