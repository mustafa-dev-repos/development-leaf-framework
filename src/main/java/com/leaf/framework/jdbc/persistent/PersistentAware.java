package com.leaf.framework.jdbc.persistent;

import java.sql.SQLException;

import com.baselib.exception.TechnicalException;
import com.leaf.framework.jdbc.resultset.ResultSet;

/**
 * 
 * @Auhtor: Mustafa   
 */



//TODO persistent aware must extend transaction Manager.. 
// remove transactional operations from this class..

public interface PersistentAware {

	public ResultSet executeQuery() throws TechnicalException, SQLException;
	
	public int executeUpdate() throws TechnicalException, SQLException;

	public void setDataSourceName(String dataSourceName);

	public void reset();

    public void beginTransaction() ;
    
    public boolean isInTransaction();

    public void commitTransaction() throws Exception;

    public void rollbackTransaction() throws Exception;

}
