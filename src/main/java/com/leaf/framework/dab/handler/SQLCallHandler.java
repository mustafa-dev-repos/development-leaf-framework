package com.leaf.framework.dab.handler;

import com.leaf.framework.dab.context.DefaultProcedureContext;

public interface SQLCallHandler {

	public Object handleCall(DefaultProcedureContext context) throws Exception;

}
