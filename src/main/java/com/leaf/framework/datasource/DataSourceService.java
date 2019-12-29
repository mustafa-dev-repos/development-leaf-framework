package com.leaf.framework.datasource;

import javax.sql.DataSource;

import com.baselib.bean.Bean;
import com.baselib.bean.lifecycle.Initialize;
import com.leaf.framework.datasource.exception.DataSourceException;
import com.leaf.framework.datasource.module.DataSourceModule;
import com.leaf.framework.datasource.module.DataSourceModuleOrder;
import com.leaf.framework.jdbc.exception.ConnectionException;

/**
 * 
 * @Auhtor: Mustafa   
 */

@Bean(implementedBy=DefaultDataSourceService.class, module = DataSourceModule.class , order = DataSourceModuleOrder.DATASOURCE_SERVICE_ORDER)
public interface DataSourceService {
	
	@Initialize(loadOnStartup = true)
	public void initialize();
	
    public DataSource getDataSource(String jndiName) throws DataSourceException, ConnectionException;
	
}
