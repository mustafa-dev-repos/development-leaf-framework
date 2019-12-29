package com.leaf.framework.jdbc.persistent.callable;

import com.baselib.bean.Bean;
import com.baselib.module.system.SystemDefaultModule;
import com.leaf.framework.jdbc.persistent.PersistentAware;
import com.leaf.framework.jdbc.resultset.ProcedureResultSet;

/**
 *
 * 
 * @Auhtor: Mustafa   
**/

@Bean(implementedBy=CallablePersistent.class, module= SystemDefaultModule.class, singleton = false)

public interface ProcedureCaller extends CallableParameterAware , PersistentAware ,ProcedureResultSet{

	public void setProcedureName(String procName);
	public void setFunctionName(String functionName);
	
}
