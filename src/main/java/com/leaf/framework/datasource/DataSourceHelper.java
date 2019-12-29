package com.leaf.framework.datasource;

import javax.sql.DataSource;

import com.baselib.bean.container.BeanHelper;
import com.leaf.framework.datasource.exception.DataSourceException;
import com.leaf.framework.jdbc.exception.ConnectionException;

/**
 * holds Reference to the JNDI resources..
 * 
 * @Auhtor: Mustafa   
 */

public class DataSourceHelper
{

    private  DataSourceHelper()
    {
    	
    }
    
    public static DataSourceService getDataSourceService()
    {
    	return BeanHelper.getBean(DataSourceService.class);
    }

    public static final DataSource getDataSource(String jndiName) throws DataSourceException, ConnectionException
    {

    	return getDataSourceService().getDataSource(jndiName);
    }
    

}
