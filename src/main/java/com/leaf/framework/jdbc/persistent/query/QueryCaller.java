package com.leaf.framework.jdbc.persistent.query;

import com.baselib.bean.Bean;
import com.baselib.module.system.SystemDefaultModule;
import com.leaf.framework.jdbc.persistent.PersistentAware;


/**
 * 
 * @Auhtor: Mustafa   
 */

@Bean(implementedBy = PreparedPersistent.class, module= SystemDefaultModule.class, singleton = false)

public interface QueryCaller extends PreparedParameterAware , PersistentAware {

	public void setSql(String sql);
}
