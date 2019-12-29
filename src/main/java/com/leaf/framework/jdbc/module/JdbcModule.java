package com.leaf.framework.jdbc.module;

import com.baselib.bean.binder.Binder;
import com.baselib.moduletype.Module;
import com.baselib.moduletype.system.SystemModuleType;
import com.leaf.framework.jdbc.persistent.callable.CallablePersistent;
import com.leaf.framework.jdbc.persistent.callable.ProcedureCaller;
import com.leaf.framework.jdbc.persistent.query.PreparedPersistent;
import com.leaf.framework.jdbc.persistent.query.QueryCaller;
import com.leaf.framework.module.ModuleConstants;

/**
 * 
 * @Auhtor: Mustafa   
 */


@SystemModuleType(order = ModuleConstants.JDBC_MODULE_ORDER)

public class JdbcModule implements Module {

	public void bindBeans(Binder binder) {
		
		binder.
		bind(ProcedureCaller.class).to(CallablePersistent.class).
		bind(QueryCaller.class).to(PreparedPersistent.class);
		
	}

}
