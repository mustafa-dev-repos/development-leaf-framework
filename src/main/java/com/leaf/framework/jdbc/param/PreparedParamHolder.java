package com.leaf.framework.jdbc.param;

import java.util.ArrayList;
import java.util.List;

import com.leaf.framework.jdbc.statement.StatementType;

/**
 * 
 * @Auhtor: Mustafa   
 */


//TODO : performance working..

public class PreparedParamHolder {

	protected List<Object> params = new ArrayList<Object>();
	
	protected StatementType statementType; 

	protected int lastIndex = 0 ; 

	protected PreparedParamHolder(){
		
		this.statementType = StatementType.QUERY ;
	} 

	public void addAll(List<Object> objList) {
		 
		lastIndex += (objList== null ? 0 : objList.size()) ;
		params = objList;
	}

	public void add(Object obj) {
		 
		lastIndex++;
		params.add(obj);
	}
	
	public int getParamCount() {

		return params.size();
	}

	/**
	 * @param statementType the statementType to set
	 */
	public void setStatementType(StatementType statementType) {

		this.statementType = statementType;
	}

	public Object get(int index) {

		return this.params.get(index);
	}
	
}
