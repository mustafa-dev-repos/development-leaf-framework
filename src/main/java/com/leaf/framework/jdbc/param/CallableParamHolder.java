package com.leaf.framework.jdbc.param;

import java.util.ArrayList;
import java.util.List;

import com.leaf.framework.jdbc.statement.StatementType;

/**
 * 
 * @Auhtor: Mustafa   
 */


public class CallableParamHolder extends PreparedParamHolder implements ProcedureParamHolder {

	private List<Param> outParams = new ArrayList<Param>(5);

	public CallableParamHolder(List<Object> allParams, List<Param> outParams) {

		super();

		if (allParams != null && allParams.size() > 0 ) {
			this.params = allParams;
			this.outParams = outParams;
			this.lastIndex = allParams.size();
		}
		
		this.statementType = StatementType.PROCEDURE;
	}

	public CallableParamHolder() {
		
		super();
		
		this.statementType = StatementType.PROCEDURE;
	}
	
	public List<Param> getOutParams() {
		
		return outParams;
	}
	
	public void bindOutToInParam(int typeOfOutParam) {

		bindOutToInParam(typeOfOutParam, null);		
	}

	public void bindOutToInParam(int typeOfOutParam, String typeName) {

		// wrap previous param in  InOutParam container..
		int currentIndex = lastIndex - 1;
		Object activeObject = params.get(currentIndex);

		InOutParam param = null;

		param = new InOutParam(activeObject, typeOfOutParam, lastIndex, typeName);
		
		outParams.add(param);
		params.set(lastIndex - 1, param);
	}

	public void addOutParam(int typeofOutParameter) {

		addOutParam(typeofOutParameter,null);
	}
	
	public void addOutParam(int typeofOutParameter, String typeName) {
		 
		OutParam param = new OutParam(typeofOutParameter, lastIndex + 1);

		lastIndex++;

		outParams.add(param);
		params.add(param);
	}

}
