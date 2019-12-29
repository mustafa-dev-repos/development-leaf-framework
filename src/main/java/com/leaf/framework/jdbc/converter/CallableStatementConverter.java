package com.leaf.framework.jdbc.converter;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.leaf.framework.jdbc.param.Param;

/**
 * 
 * @Auhtor: Mustafa   
 */

public class CallableStatementConverter {

	public static Map<Integer, Object> convert(CallableStatement statement, List<Param> outParams) throws SQLException  {
 
		if (outParams == null  || outParams.size() == 0) 
			return null;

		Map<Integer, Object> returnParams = new HashMap<Integer, Object>(outParams.size(), 1f);
		
		for (int i = 0 ; i < outParams.size()  ; i++ ) {

			Param param = outParams.get(i);
				
			returnParams.put(param.getParamIndex(), statement.getObject(param.getParamIndex()));

		}
		
		return returnParams;

	}

}
