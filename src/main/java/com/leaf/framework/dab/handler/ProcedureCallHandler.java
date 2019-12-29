package com.leaf.framework.dab.handler;

import java.text.MessageFormat;

import com.baselib.exception.TechnicalException;
import com.baselib.util.ClassUtil;
import com.leaf.framework.dab.context.DefaultProcedureContext;
import com.leaf.framework.jdbc.param.ParamHolderHelper;
import com.leaf.framework.jdbc.param.ProcedureParamHolder;
import com.leaf.framework.jdbc.persistent.PersistentManager;
import com.leaf.framework.jdbc.persistent.callable.ProcedureCaller;

public class ProcedureCallHandler implements SQLCallHandler {

	protected static final String ERROR_TO_MANY_RESULT_FOR_PRIMITIVE = "[{0}] primitive class can not be assigned from [{1}] number of out values";

	protected static final String CLASS_NAME = ProcedureCallHandler.class.getName(); 
	
	public Object handleCall(DefaultProcedureContext context) throws Exception {

		ProcedureCaller caller = PersistentManager.getProcedureCaller();

		if (context.isProcedureCall()) {
			
			caller.setProcedureName(context.getProcedureName());

		} else {
			
			caller.setFunctionName(context.getProcedureName());
		}

		if (context.getJndiName() != null) {
			
			caller.setDataSourceName(context.getJndiName());
		}
		
		if (context.hasParameters()) {
			
			ProcedureParamHolder paramHolder = ParamHolderHelper.getProcedureParamHolder(context.getParams() , context.getOutAndInOutParams());
			caller.setParams(paramHolder);
		}

		caller.executeUpdate();

		Class<?> returnType = context.getMethod().getReturnType();
		
		if (returnType != null && returnType != void.class ) {
			
			if (ClassUtil.isSingleValued(returnType) ) {
					
					if (context.hasOutandInOutParams() ) {
						
						if (context.getOutAndInOutParams().size() == 1 ) {

							int returnIndex = context.getOutAndInOutParams().get(0).getParamIndex();
							return caller.getObject(returnIndex);
						}
						else {
							
			            	String message = MessageFormat.format(ERROR_TO_MANY_RESULT_FOR_PRIMITIVE, new Object[]{returnType.getName(), context.getOutAndInOutParams().size() });
			            	throw new TechnicalException(CLASS_NAME, message);
						}
					}
					else {
						return null;
					}	
				
			}
			else {
				//TODO  handle here.
            	throw new TechnicalException(CLASS_NAME, "TODO feature.. not implemented yet...");
				
			}
			
		}
			
			
		return null;
		
		
	}

}
