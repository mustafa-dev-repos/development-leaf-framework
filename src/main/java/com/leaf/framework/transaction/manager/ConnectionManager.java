package com.leaf.framework.transaction.manager;

import java.sql.Connection;
import java.sql.SQLException;

import com.baselib.bean.Bean;
import com.baselib.module.system.SystemDefaultModule;

@Bean(implementedBy = DefaultConnectionManager.class, module = SystemDefaultModule.class, singleton=false)

/**
 * 
 * @Auhtor: Mustafa   
 */

public interface ConnectionManager {
	
	public boolean isConnectionAvailable();
	public boolean isConnectionAvailableFor(String dataSourceName);

	public void commit(boolean autoCommitMode) throws SQLException;
	public void rollback(boolean autoCommitMode) throws SQLException;
	public void close() throws SQLException;

	public void setConnection(String dataSourceName, Connection connection) throws Exception;
	public Connection getConnection(String dataSourceName) throws Exception;
 
}
