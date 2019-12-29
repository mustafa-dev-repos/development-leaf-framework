package com.leaf.framework.jdbc.resultset;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Timestamp;


/**
 * 
 * @Auhtor: Mustafa   
 */

public class DefaultResultSet implements ResultSet {

	java.sql.ResultSet result;

	public DefaultResultSet(java.sql.ResultSet result) {

		this.result = result;
	}

	public final BigDecimal getBigDecimal(int loc) throws SQLException {

		return result.getBigDecimal(loc);
	}

	public BigDecimal getBigDecimal(String name) throws SQLException {

		return result.getBigDecimal(name);
	}

	public final double getDouble(int loc) throws SQLException {

		return result.getDouble(loc);
	}

	public double getDouble(String name) throws SQLException {

		return result.getDouble(name);
	}
	
	public int getInt(int loc) throws SQLException {

		return result.getInt(loc);
	}

	public int getInt(String name) throws SQLException {

		return result.getInt(name);
	}
	
	public long getLong(int loc) throws SQLException {

		return result.getLong(loc);
	}

	public long getLong(String name) throws SQLException {

		return result.getLong(name);
	}

	public String getLongString(int loc) throws SQLException {

		Clob clobValue = null;
		
		clobValue = result.getClob(loc);

		if (clobValue != null) {
			
			int len = new Long(clobValue.length()).intValue();
			return clobValue.getSubString(1, len);
		}
		
		return null;
	}

	public String getLongString(String name) throws SQLException, IOException {

		Clob clobValue = null;
		
		clobValue = result.getClob(name);

		if (clobValue != null) {
			int len = new Long(clobValue.length()).intValue();
			return clobValue.getSubString(1, len);
		}

		return null;
	}

	public String getString(int loc) throws SQLException {

		return result.getString(loc);
	}

	public String getString(String name) throws SQLException {

		return result.getString(name);
	}

	public Timestamp getTimeStamp(int loc) throws SQLException {

		return result.getTimestamp(loc);
	}

	public Timestamp getTimeStamp(String name) throws SQLException {

		return result.getTimestamp(name);
	}

	public boolean next() throws SQLException {

		return result.next();
	}
	
}
