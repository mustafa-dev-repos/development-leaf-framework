package com.leaf.framework.jdbc.param;

import java.util.List;

/**
 * 
 * @Auhtor: Mustafa   
 */


//TODO use bean helper to get beans..
public class ParamHolderHelper {

	public static PreparedParamHolder getQueryParamHolder() {
		
		return new PreparedParamHolder(); 
	} 

	public static ProcedureParamHolder getProcedureParamHolder(List<Object> allParams, List<Param> outParams){
		
		return new CallableParamHolder(allParams, outParams); 	
	}
	
	public static ProcedureParamHolder getProcedureParamHolder() {

		return new CallableParamHolder(); 
	} 

}
