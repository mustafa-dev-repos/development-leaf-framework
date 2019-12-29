package com.leaf.framework.jdbc.param;

import java.util.List;
/**
 * 
 * @Auhtor: Mustafa   
 */


public interface ProcedureParamHolder {

	public List<Param> getOutParams();
	
	public int getParamCount() ;
	
	public Object get(int index);
	
	public void addAll(List<Object> objList);
	
	public void add(Object obj);
	
	public void bindOutToInParam(int typeOfOutParam);
	
	public void bindOutToInParam(int typeOfOutParam, String typeName);

	public void addOutParam(int typeofOutParameter);	

	public void addOutParam(int typeofOutParameter, String typeName);

}
