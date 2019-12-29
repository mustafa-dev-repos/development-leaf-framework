package com.leaf.framework.jdbc.persistent.query;
import java.util.List;

/**
 * 
 * @Auhtor: Mustafa   
 */

public interface PreparedParameterAware {

	public void setNull(int type);

	public void setParam(Object value); 

	public void setParams(List<Object> params);

	public void setParams(Object[] params);

	public void setParam(int value);

	public void setParam(long value); 

	public void setParam(float value);

	public void setParam(double value);

}
