package com.leaf.framework.datasource.jndi;

import java.util.Map;

import javax.sql.DataSource;

import com.baselib.bean.Bean;
import com.leaf.framework.datasource.DefaultDataSourceHolder;
import com.leaf.framework.datasource.module.DataSourceModule;
import com.leaf.framework.datasource.module.DataSourceModuleOrder;


/**
 * Holds Reference to the JNDI resources..
 * 
 * @Auhtor: Mustafa   
 */

@Bean(implementedBy = DefaultJndiService.class, module = DataSourceModule.class, order = DataSourceModuleOrder.JNDI_SERVICE_ORDER )

public interface JndiService  {
	
	   public Map<String,DefaultDataSourceHolder> getDataSource();
       public DataSource getDataSource(String jndiName);

}
