package com.leaf.framework.dab.context;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import com.baselib.exception.TechnicalException;
import com.leaf.framework.dab.annotation.Procedure;
import com.leaf.framework.dab.annotation.SQLParamInOut;
import com.leaf.framework.dab.annotation.SQLParamOut;
import com.leaf.framework.dab.call.ProcedureType;
import com.leaf.framework.jdbc.param.InOutParam;
import com.leaf.framework.jdbc.param.OutParam;
import com.leaf.framework.jdbc.param.Param;
import com.leaf.framework.util.JdbcTypeUtil;

/**
 * 
 * @Auhtor: Mustafa   
 */

public class DefaultProcedureContext {

	protected static final String ERROR_PARAMETER_INDEX_OVER_OF_MAX = "According your procudure call declaration, parameter index can not be bigger than [{0}]";

	protected static final String ERROR_PARAM_INDEX_BELOW_OF_MIN = "Parameter index must be bigger than zero";
	
	private Procedure storedProcedure;

	private Object[] invocationValues;

	private Method method;

	private String defaultPackagePrefix;

	private ProcedureType procedureType;  // query or procedure

    // TODO replace following variables with paramholder
	List<Object> params;
	List<Param>  outAndInOutParams;
	
	private int paramCount;

	// only out params, inout params not included..
	private int outParamCount;
	private int inAndInOutParamCount;

	
	private static final String CLASS_NAME = DefaultProcedureContext.class.getName();
	
	public DefaultProcedureContext(Method method, Procedure storedProcedure, Object[] invocationValues, String defaultPackagePrefix) {

		this.method = method;
		
		this.storedProcedure = storedProcedure;
		this.invocationValues = invocationValues;
		this.defaultPackagePrefix = defaultPackagePrefix;

		this.procedureType = getProcedureType(method, storedProcedure);

		this.paramCount = calculateTotalParamCount(storedProcedure, invocationValues) ;
		this.outParamCount = calculateOutParamCount(storedProcedure);
		this.inAndInOutParamCount = (invocationValues == null ? 0 : invocationValues.length);
		
		//TODO refactor into paramHolder..
		
		this.params = new ArrayList<Object>(this.paramCount);
		this.outAndInOutParams =  new ArrayList<Param>();

		for (int i = 0 ; i < paramCount ; i++) {
			
			this.params.add(null);
		}
		
		constructParams(storedProcedure);
	}

	private int calculateTotalParamCount(Procedure storedProcedure, Object[] invocationValues) {
	
		return calculateProcedureParamCount(invocationValues) + calculateOutParamCount(storedProcedure);
		
	}
	
	public boolean isProcedureCall() {
		
		if (ProcedureType.PROCEDURE.equals(procedureType) ||
			ProcedureType.PACKAGE_PROCEDURE.equals(procedureType)) {
			return true;
		}
			return false;
	}

	public ProcedureType getProcedureType(Method method, Procedure storedProcedure) {
		
		if (storedProcedure == null) {

			if (Void.class.equals(method.getReturnType()) || void.class.equals(method.getReturnType()) ){
				return ProcedureType.PROCEDURE;
			}
				return ProcedureType.FUNCTION;
		}
			return storedProcedure.procedureType();	
	}

	public int getParamCount(){
		
			return  paramCount ;
	}
	
	public boolean hasParameters(){
		
		return  paramCount > 0 ? true : false ;
	}

	private int calculateProcedureParamCount(Object[] invocationValues){

		if (invocationValues != null && invocationValues.length > 0) {
			return invocationValues.length;
		}

			return 0;
	}

	private int calculateOutParamCount(Procedure storedProcedure) {

		int outParamCount  = 0;
		
		if (storedProcedure != null) {
			
			SQLParamOut[] params = storedProcedure.outParams();

			if (params[0].paramIndex() != -1 ) {
				outParamCount = params.length;
			}
		}
		
		return isProcedureCall() ? outParamCount : outParamCount + 1;
	}

	private void constructParams(Procedure storedProcedure) {

		// out params has priority over input params because of they have paramIndex property..
		constructOutParams(storedProcedure);

		constructInputParams(storedProcedure);
	}

	private void constructOutParams( Procedure storedProcedure ) {

		if (storedProcedure == null)
			return ;

		boolean isFunction = !isProcedureCall();

		if (isFunction) {

			OutParam param = new OutParam(JdbcTypeUtil.getJdbcType(method.getReturnType()), 1);
			params.set(0 , param);  
			outAndInOutParams.add(param);
		}
				
		SQLParamOut[] outParams = storedProcedure.outParams();

		if (outParams[0].paramIndex() != -1 ) {

			for (int i = 0; i < outParams.length; i++) {

				checkParameterIndex(outParams[i]);
				
				OutParam param = new OutParam(JdbcTypeUtil.getJdbcType(outParams[i].returnType()), outParams[i].paramIndex());
				this.params.set(outParams[i].paramIndex() + (isFunction ? 0 : -1), param);

				this.outAndInOutParams.add(param);
			}
		}
	}

	private void checkParameterIndex(SQLParamOut outParams) {

		if (outParams.paramIndex() < 1) {

			String message = MessageFormat.format(ERROR_PARAM_INDEX_BELOW_OF_MIN, new Object[]{paramCount });
			throw new TechnicalException(CLASS_NAME, message);
		}
		
		if (outParams.paramIndex() > paramCount) {

			String message = MessageFormat.format(ERROR_PARAMETER_INDEX_OVER_OF_MAX, new Object[]{paramCount });
			throw new TechnicalException(CLASS_NAME, message);
		}
	}

	private void constructInputParams(Procedure storedProcedure) {
	

		if (invocationValues != null && invocationValues.length > 0) {

			Annotation[][] parameterAnnotations = method.getParameterAnnotations();

			SQLParamInOut param;

			int paramIndex = 0;

			//Caution : just out params , not out and inout params 
			boolean hasOutParams = false ;
			
			if (isProcedureCall()) {

				hasOutParams = (this.outParamCount > 0 ? true : false );
			}
			else {
				// for function calls set true.., first param is out param for functions..
				hasOutParams = true;
			}

			for (int i = 0; i < this.inAndInOutParamCount; i++) {

				if (hasOutParams) {
					 
					while (paramIndex < this.paramCount && this.params.get(paramIndex) != null) {
						
					paramIndex++;
					}
				}

				// exit if paramIndex reached to length
				if (paramIndex == paramCount)
					break;
				
				// reset Param for each argument
				param = null;

				Annotation[] paramAnnos = parameterAnnotations[i];

				for (int j = 0; j < paramAnnos.length; j++) {

					Annotation paramAnnotation = paramAnnos[j];

					if (paramAnnotation instanceof SQLParamInOut) {
						param = (SQLParamInOut) paramAnnotation;
						break;
					}
				}
				
				// set with default index
				if (param != null) {

					InOutParam inOutparam = new InOutParam(invocationValues[i], JdbcTypeUtil.getJdbcType(param.returnType()), paramIndex );

					params.set(paramIndex, inOutparam);	

					outAndInOutParams.add(inOutparam);

					paramIndex++;
					
				} else {

					params.set(paramIndex, invocationValues[i]);	
					paramIndex++;
				}
			}
		}
	}

	/**
	 * @return the invocationType
	 */
	public ProcedureType getProcedureType() {
		
		return procedureType;
	}

	public String getJndiName() {
		
		return (storedProcedure == null || "".equals(storedProcedure.jndiName())) ? null : storedProcedure.jndiName();
	}

	/**
	 * @return the defaultPackagePrefix
	 */
	public String getDefaultPackagePrefix() {
		return defaultPackagePrefix;
	}

	public String getProcedureName() {

		if (storedProcedure == null) {

			return (getDefaultPackagePrefix() == null ? method.getName() : (getDefaultPackagePrefix() + "." + method.getName()) );
		} else {

			return (getPackagePrefix() == null ? getProcNameFromAnnotation() : (getPackagePrefix() + "." + getProcNameFromAnnotation()) );
		}
	}

	private String getProcNameFromAnnotation() {
	
			return "".equals(storedProcedure.name()) ? getProcNameFromMethod() : storedProcedure.name();   
	}

	private String getProcNameFromMethod() {
		
		return  method.getName();   
	}
	
	private String getPackagePrefix () {

		if (storedProcedure == null || 
		    "".equals(storedProcedure.prefix())) {

			return getDefaultPackagePrefix();
		} else {

			return storedProcedure.prefix();
		}
	}

	/**
	 * @return the params
	 */
	public List<Object> getParams() {
		return params;
	}

	/**
	 * @return the storedProcedure
	 */
	public Procedure getStoredProcedure() {
		return storedProcedure;
	}

	/**
	 * @return the outParams
	 */
	public List<Param> getOutAndInOutParams() {
		return outAndInOutParams;
	}
	
	public boolean hasOutandInOutParams(){
		
		return  (outAndInOutParams != null && outAndInOutParams.size() > 0) ? true : false;
	}

	public Method getMethod() {
		return method;
	}

}