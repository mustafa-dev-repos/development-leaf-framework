package com.leaf.framework.jdbc.resultset;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;

/**
 * 
 * @Auhtor: Mustafa   
 */

//TODO : PreparedresultSet and this interface has common method , creatre super interface

public interface ProcedureResultSet {

       public abstract BigDecimal getBigDecimal(int loc) throws SQLException;

       public abstract double getDouble(int loc) throws SQLException;

       public abstract int getInt(int loc) throws SQLException ;

       public abstract long getLong(int loc) throws SQLException;

       public abstract String getLongString(int loc) throws SQLException, IOException;

       public abstract String getString(int loc) throws SQLException;

       public abstract Timestamp getTimeStamp(int loc) throws SQLException;

       public String getClob(int loc) throws SQLException;

       public Map<Integer, Object> getReturnedObjects();
       
       public Object getObject(int loc);

}

