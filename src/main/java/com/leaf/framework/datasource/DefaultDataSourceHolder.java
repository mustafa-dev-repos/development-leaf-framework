package com.leaf.framework.datasource;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.text.MessageFormat;

import javax.sql.DataSource;

import com.baselib.log.LogHelper;
import com.leaf.framework.dab.call.VendorType;

/**
 * 
 * @Auhtor: Mustafa   
 */

public class DefaultDataSourceHolder {

	private DataSource dataSource;

	private String jndiName;

	private VendorType vendor;
	
	private static final String CLASS_NAME = DefaultDataSourceHolder.class.getName();

	private LogHelper log = LogHelper.getLog();

	private static final String ERROR_GETTING_CONNECTION = "Error occured during getting connection from [{0}] data source. Error : [{1}] ";

	public DefaultDataSourceHolder(String jndiName, DataSource dataSource) {

		super();
		
		this.dataSource = dataSource;
		this.jndiName = jndiName;
	}
	
	private void setVendor() {

		try {

			Connection con = this.dataSource.getConnection();
		
			DatabaseMetaData metaData = con.getMetaData();
			
			String databaseName = metaData.getDatabaseProductName();
	
			if ("Oracle".equals(databaseName)) {
				vendor = VendorType.ORACLE;
				
			} else if ("Microsoft SQL Server".equals(databaseName)) {
				vendor = VendorType.MS_SQL_SERVER;
			}
        
		
		 } catch(Throwable t) {

			 	String message = MessageFormat.format(ERROR_GETTING_CONNECTION, new Object[]{jndiName, t.getMessage()});
			 	log.error(message);
		 }
		
	}

	/**
	 * @return the dataSource
	 */
	public DataSource getDataSource() {
		
		return dataSource;
	}

	/**
	 * @param dataSource the dataSource to set
	 */
	public void setDataSource(DataSource dataSource) {
		
		this.dataSource = dataSource;
	}

	/**
	 * @return the jndiName
	 */
	public String getJndiName() {
		return jndiName;
	}

	/**
	 * @param jndiName the jndiName to set
	 */
	public void setJndiName(String jndiName) {
		this.jndiName = jndiName;
	}


}
