package com.leaf.framework.jdbc.param;

/**
 * 
 * @Auhtor: Mustafa   
 */

public class InOutParam extends Param {

	private static final long serialVersionUID = 881719704078778429L;

	private Object inputValue;

	public InOutParam(Object val, int type, int paramIndex ) {

		super( type, paramIndex);
		
		this.inputValue = val;
	}

	public InOutParam(Object val,int type, int paramIndex, String typeName) {

		super(type,  paramIndex, typeName );
		this.inputValue = val;
		
	}

	/**
	 * @return the inputValue
	 */
	public Object getInputValue() {
		return inputValue;
	}

	/**
	 * @param inputValue the inputValue to set
	 */
	public void setInputValue(Object inputValue) {
		this.inputValue = inputValue;
	}

}
