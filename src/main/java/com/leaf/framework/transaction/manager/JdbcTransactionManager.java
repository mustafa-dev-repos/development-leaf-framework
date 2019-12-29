package com.leaf.framework.transaction.manager;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;

import com.baselib.bean.container.BeanHelper;
import com.baselib.exception.TechnicalException;
import com.leaf.framework.util.TransactionUtil;

/**
 * 
 * @Auhtor: Mustafa   
 */

public class JdbcTransactionManager implements TransactionManager {
	
    public final String ERROR_UNABLE_AUTOCOMMIT_MODE = "Unable to set AutoCommit mode";

    public final String ERROR_TRANSACTION_ROLLED_BACK = "Can not [{0}] because of transaction already rolled back. There is no valid transaction";

    private ConnectionManager connectionManager = BeanHelper.getBean(ConnectionManager.class) ;   

	private boolean transactionMode = false;

	private boolean rolledBack = false;
	
    private static final String CLASS_NAME = JdbcTransactionManager.class.getName();
    
    private enum TransactionOperationType {
    	BEGIN_TRANSACTION,COMMIT_TRANSACTION, CLOSE_CONNECTION, GET_CONNECTION
    }
    
    public final void beginTransaction() {

    	checkTransactionValid(TransactionOperationType.BEGIN_TRANSACTION);
    	
		transactionMode = true;
	}

	private final void checkTransactionValid(TransactionOperationType operationType){

		// if transaction is rolled back
		if (rolledBack) {
			
            String msg = MessageFormat.format(ERROR_TRANSACTION_ROLLED_BACK, new Object[] { operationType.name().replace('_', ' ') } );
    		throw new TechnicalException(CLASS_NAME , msg);
	  	}

	}

    public final boolean isConnectionAvailable() {

    	checkTransactionValid(TransactionOperationType.GET_CONNECTION);
    			
		return connectionManager.isConnectionAvailable();
	}
	
	public final void commitTransaction() throws Exception {
		
	  try {
	    	checkTransactionValid(TransactionOperationType.COMMIT_TRANSACTION);

			transactionMode = TransactionUtil.checkCommitable(this);
				
	    	if (transactionMode) {

	    		connectionManager.commit(true);
	    		transactionMode = false;
            
	    		connectionManager.close();
	    	}
            
          } catch (Exception e) {
                   throw new TechnicalException(CLASS_NAME, e.getMessage(), e);
        }
	}

	/**
     * Verify if the connection must be released, and, if need be, releases it
     * 
     * @throws TechnicalException
     *             this exception is sent back if a SQL error arises
     */
    public final void closeConnection() throws SQLException {
    	
        if (!transactionMode && connectionManager.isConnectionAvailable() ) {
        	 connectionManager.close();
        }
    }

	public final Connection getConnection(String dataSourceName) throws Exception {

		Connection connection = null;

		checkTransactionValid(TransactionOperationType.CLOSE_CONNECTION);

		connection = connectionManager.getConnection(dataSourceName);
        		
        try {

        	// Set the transactionMode value
            if (transactionMode && (connection != null && connection.getAutoCommit()) ) {

            	connection.setAutoCommit(false);
            }
        } catch (SQLException e) {
        		throw new TechnicalException(CLASS_NAME , ERROR_UNABLE_AUTOCOMMIT_MODE, e);
        }
		
		return connection;
	}

	public final boolean isInTransaction() {

		return transactionMode;
	}

	
	public final void rollbackTransaction() {

       try {
    	   
    	   transactionMode = TransactionUtil.checkRollable(this);

    	   if (transactionMode) {
            
    		   connectionManager.rollback(true);
    		   transactionMode = false;
    		   rolledBack = true;
    		   
    		   closeConnection();
    	   }
    	   else {
    		   
    		   transactionMode = false;
    	   }
            
           } catch (Exception e) {
            throw new TechnicalException(CLASS_NAME , e.getMessage(), e);
        }
	}

	/**
	 * @return the connectionManager
	 */

}
