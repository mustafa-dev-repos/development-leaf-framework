package com.leaf.framework.datasource.module;

import com.baselib.bean.binder.Binder;
import com.baselib.moduletype.Module;
import com.baselib.moduletype.system.SystemModuleType;
import com.leaf.framework.datasource.DataSourceService;
import com.leaf.framework.datasource.DefaultDataSourceService;
import com.leaf.framework.datasource.jndi.DefaultJndiService;
import com.leaf.framework.datasource.jndi.JndiService;
import com.leaf.framework.module.ModuleConstants;

@SystemModuleType(order = ModuleConstants.DATASOURCE_MODULE_ORDER)

public class DataSourceModule implements Module {

	public void bindBeans(Binder binder) {

		binder.bind(JndiService.class).to(DefaultJndiService.class).
			   bind(DataSourceService.class).to(DefaultDataSourceService.class);
		
	}

}
