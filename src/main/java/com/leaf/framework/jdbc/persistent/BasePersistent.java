package com.leaf.framework.jdbc.persistent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.baselib.bean.container.BeanHelper;
import com.baselib.config.ConfigHelper;
import com.baselib.exception.TechnicalException;
import com.leaf.framework.jdbc.statement.StatementType;
import com.leaf.framework.transaction.manager.GlobalTransactionHelper;
import com.leaf.framework.transaction.manager.TransactionManager;
import com.leaf.framework.util.TransactionUtil;

/**
 * 
 * @Auhtor: Mustafa 
 * 
 */

public abstract class BasePersistent implements PersistentAware {

	protected static final String ERROR_PARAMETER_NOT_SUPPORTED = "Parameter type not supported : [{0}]";

	protected static final String DEBUG_PARAM = "Param : [{0}]";

	protected static final String DEBUG_QUERY = "Query : [{0}]";

	protected String dataSourceName;

	protected TransactionManager transactionManager ;

	protected boolean isGlobalTransactionAvailable = false;

	protected PreparedStatement statement = null;

	protected ResultSet result = null;

	protected StatementType statementType;

	private static final String CLASS_NAME = BasePersistent.class.getName();

	public BasePersistent() {
		
		isGlobalTransactionAvailable = GlobalTransactionHelper.isInTransaction();
		
		if (isGlobalTransactionAvailable) {
			transactionManager = GlobalTransactionHelper.getTransactionManager();
		}
	}

	
	public BasePersistent(String dataSourceName) {

		this.dataSourceName = dataSourceName;
	}

	public void reset() {

		this.dataSourceName = ConfigHelper.getConfigManager().getDefaultJndiName();

		//TODO release required resources..
	}

	public void setDataSourceName(String dataSourceName) {

		this.dataSourceName = dataSourceName;
	}

	protected void setStatementType(StatementType statementType) {

		this.statementType = statementType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.leaf.framework.jdbc.datasource.TransactionAware#beginTransaction()
	 */
	public void beginTransaction() {
		
		// start local transaction if there is no global transaction
		if (!isGlobalTransactionAvailable) {
			
			// check again whether a global transaction already started 
			isGlobalTransactionAvailable = GlobalTransactionHelper.isInTransaction();
			
			if (isGlobalTransactionAvailable) {
				
				// get global transaction..
				transactionManager = GlobalTransactionHelper.getTransactionManager();
			}
			else {
				// start a new local transaction..
				transactionManager = BeanHelper.getBean(TransactionManager.class);	
				
			}
		}
			
			transactionManager.beginTransaction();
			
		
	}

	//TODO refactor to transactionhelper
	public TransactionManager getTransactionManager() {
			
			if (GlobalTransactionHelper.isInTransaction()) {
				
				// get global transaction..
				return GlobalTransactionHelper.getTransactionManager();
			}
			else {
				// start a new local transaction..
				return BeanHelper.getBean(TransactionManager.class);	
				
			}
		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.leaf.framework.jdbc.datasource.TransactionAware#commitTransaction()
	 */
	public void commitTransaction() throws Exception {

		if (!isGlobalTransactionAvailable) {

			if (TransactionUtil.checkCommitable( transactionManager )) {

				closeResultSet();
				
				closeStatement();

				transactionManager.commitTransaction();
			}
		}
	}

	/**
	 * Verify if the connection must be released, and, if need be, releases it
	 * 
	 * @throws TechnicalException
	 *             this exception is sent back if a SQL error arises
	 */
	protected void closeConnection() throws SQLException {

		if (!isGlobalTransactionAvailable) {
			
			transactionManager.closeConnection();
		}		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.leaf.framework.jdbc.datasource.TransactionAware#getConnection()
	 */
	public Connection getConnection() throws Exception {
		
		if (transactionManager == null) {
			
			// dont start transaction..
			transactionManager = BeanHelper.getBean(TransactionManager.class);
		} 

		return transactionManager.getConnection(dataSourceName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.leaf.framework.jdbc.datasource.TransactionAware#isInTransaction()
	 */
	public boolean isInTransaction() {
		return transactionManager.isInTransaction();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.leaf.framework.jdbc.datasource.TransactionAware#rollbackTransaction()
	 */
	public void rollbackTransaction() throws Exception {
		
		// in the case of failure local or global transaction must be rollbacked
		
		if (TransactionUtil.checkRollable( transactionManager )) {

			closeResultSet();
			closeStatement();

			transactionManager.rollbackTransaction();
		}
			isGlobalTransactionAvailable = false;
		
	}

	public final void closeResultSet() throws Exception {

		if (result != null) {

			try {

				result.close();
				
			} catch (SQLException e) {
				
				throw e;
			}
				result = null;
		}
	}

	public final void closeStatement() throws SQLException {

		if (statement != null) {

			try {

				statement.close();
				
			} catch (SQLException e) {
				
				throw e;
			}

			statement = null;
		}
	}

}