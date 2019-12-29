package com.leaf.framework.transaction.manager;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.sql.DataSource;

import com.baselib.exception.TechnicalException;
import com.leaf.framework.datasource.DataSourceHelper;

/**
 * 
 * @Auhtor: Mustafa   
 */

public class DefaultConnectionManager implements ConnectionManager {

    public final String ERROR_CLOSING_CONNECTION = "An error occurred while closing the following connection(s):";

    public final String ERROR_NOT_RETRIEVE_CONNECTION = "Could not retrieve a connection from the DataSource.";

    public final String ERROR_UNKNOWN_DATASOURCE = "The DataSource named [{0}] could not be found";

    public final String ERROR_NO_DEFAULT_DATASOURCE = "The default DataSource could not be found";
    
	private Map<String, Connection> connectionMap = new HashMap<String, Connection>(4, 1f);   

    private static final String CLASS_NAME = DefaultConnectionManager.class.getName();
	
	public boolean isConnectionAvailable() {
	
		return connectionMap.size() == 0 ? false : true;
	}

	public void commit(boolean autoCommitMode) throws SQLException {

		Connection con = null;  
		
		for (Iterator<String> iter = connectionMap.keySet().iterator(); iter.hasNext(); ) {

			con = connectionMap.get(iter.next());

			con.commit();
		}
	}

	public void close() throws SQLException {

		Connection con = null;
		SQLException exception = null;
		
		String jndiName = null;
		String message = null;

		try  {
			
			for (Iterator<String> iter = connectionMap.keySet().iterator(); iter.hasNext(); ) {

				try {
				
					jndiName = iter.next();
					
					con = connectionMap.get( jndiName );
					con.setAutoCommit(true);
					con.close();
    			
					iter.remove();

				} catch (SQLException e) {

					if (message == null) {
						
						exception = e;
						message +=  ERROR_CLOSING_CONNECTION + " " +jndiName;
					}
					else {
						message +=  " , " +jndiName;
					}
				}
			}
		}
		finally {
			
			connectionMap.clear();
		}

		if (message != null) {
			
			throw new TechnicalException(CLASS_NAME, message, exception);
		}

		
	}

	public boolean isConnectionAvailableFor(String dataSourceName) {

		return connectionMap.containsKey(dataSourceName);
	}
	
	public void setConnection(String dataSourceName, Connection connection) throws Exception {
		
		connectionMap.put(dataSourceName, connection);
	}

	public void rollback(boolean autoCommitMode) throws SQLException {

		Connection con = null;  

		for (Iterator<String> iter = connectionMap.keySet().iterator(); iter.hasNext(); ) {

			con = connectionMap.get(iter.next());

			con.rollback();
		}
	}

	public Connection getConnection(String dataSourceName) throws Exception {

        Connection connection = null;
        
        if (dataSourceName == null){
        	dataSourceName = null;//TODO get default data source name..
        }

        // set the active connection if an, or create a new one
        if (!isConnectionAvailableFor(dataSourceName)) {

            DataSource dataSource = null;

            try {

                dataSource = DataSourceHelper.getDataSource( dataSourceName );

                if (dataSource == null) {

                	String msg = null;

                    if (dataSourceName != null) {
                    	
                        msg = MessageFormat.format(ERROR_UNKNOWN_DATASOURCE, new Object[] { dataSourceName } );
                    } else {
                    	
                        msg = MessageFormat.format(ERROR_NO_DEFAULT_DATASOURCE, new Object[] {} );
                    }
                    
                    throw new TechnicalException(CLASS_NAME , msg);
                }

                connection = dataSource.getConnection();
                
                setConnection(dataSourceName, connection) ;

            } catch (SQLException e) {
                throw new TechnicalException(CLASS_NAME, ERROR_NOT_RETRIEVE_CONNECTION, e);
            }
        } else {
        	
        		connection = connectionMap.get(dataSourceName);
        }
		
        	return connection;
	}
	
	
}
