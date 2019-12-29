package com.leaf.framework.jdbc.persistent;

import com.baselib.bean.container.BeanHelper;
import com.leaf.framework.jdbc.persistent.callable.ProcedureCaller;
import com.leaf.framework.jdbc.persistent.query.QueryCaller;


/**
 * 
 * @Auhtor: Mustafa   
 */

public class PersistentManager {

	public static QueryCaller getQueryCaller() throws Exception {
		
		return BeanHelper.getBean(QueryCaller.class)  ;
	}

	public static ProcedureCaller getProcedureCaller() throws Exception {

		return BeanHelper.getBean(ProcedureCaller.class)  ;
	}
	
}