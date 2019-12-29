package com.leaf.framework.dab.handler;

import java.util.HashMap;
import java.util.Map;

import com.leaf.framework.dab.call.CallType;

public class SQLCallHandlerFactory {

	private static Map<CallType, SQLCallHandler> handler;

	static 
	{
		
	    handler = new HashMap<CallType, SQLCallHandler>(2, 1f);

	    handler.put(CallType.STORED_PROCEDURE , new ProcedureCallHandler());
	    
	    handler.put(CallType.SQL_QUERY , new QueryCallHandler());
	    
	}
	
	public static SQLCallHandler getCallHandler(CallType callType)
    {

		return handler.get( callType );
    }

}
