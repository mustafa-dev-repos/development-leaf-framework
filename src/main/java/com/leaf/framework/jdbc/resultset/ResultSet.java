package com.leaf.framework.jdbc.resultset;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * 
 * @Auhtor: Mustafa   
 */

public interface ResultSet {
	
	public abstract BigDecimal getBigDecimal(int loc) throws SQLException;
	public abstract BigDecimal getBigDecimal(String name) throws SQLException;

	public abstract double getDouble(int loc) throws SQLException;
	public abstract double getDouble(String name) throws SQLException;

	public abstract int getInt(int loc) throws SQLException ;
	public abstract int getInt(String name) throws SQLException ;

	public abstract long getLong(int loc) throws SQLException;
	public abstract long getLong(String name) throws SQLException;

	public abstract String getLongString(int loc) throws SQLException, IOException;
	public abstract String getLongString(String name) throws SQLException, IOException;

	public abstract String getString(int loc) throws SQLException;
	public abstract String getString(String name) throws SQLException;

	public abstract Timestamp getTimeStamp(int loc) throws SQLException;
	public abstract Timestamp getTimeStamp(String name) throws SQLException;

	public boolean next() throws SQLException;
	
}
