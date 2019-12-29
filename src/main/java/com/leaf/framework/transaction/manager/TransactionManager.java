package com.leaf.framework.transaction.manager;

import java.sql.Connection;
import java.sql.SQLException;

import com.baselib.bean.Bean;
import com.baselib.module.system.SystemDefaultModule;

/**
 * 
 * @Auhtor: Mustafa   
 */

@Bean( implementedBy= JdbcTransactionManager.class, module= SystemDefaultModule.class, singleton = false)

public interface TransactionManager {
	
    public void beginTransaction() ;
    
    public boolean isInTransaction();

    public void commitTransaction() throws Exception;

    public void rollbackTransaction() throws Exception;

    public boolean isConnectionAvailable();
    
	public Connection getConnection(String dataSourceName) throws Exception ;

	public void closeConnection() throws SQLException;
}
