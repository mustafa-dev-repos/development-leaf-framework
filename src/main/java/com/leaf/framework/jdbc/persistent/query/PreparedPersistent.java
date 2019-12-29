package com.leaf.framework.jdbc.persistent.query;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.baselib.config.ConfigHelper;
import com.baselib.exception.TechnicalException;
import com.baselib.log.LogHelper;
import com.baselib.util.ExceptionUtil;
import com.leaf.framework.jdbc.converter.PreparedResultSetConverter;
import com.leaf.framework.jdbc.param.NullParam;
import com.leaf.framework.jdbc.persistent.BasePersistent;
import com.leaf.framework.jdbc.persistent.callable.CallablePersistent;
import com.leaf.framework.jdbc.resultset.DefaultResultSet;
import com.leaf.framework.jdbc.statement.StatementType;

/**
 * 
 * @Auhtor: Mustafa   
 */

public class PreparedPersistent extends BasePersistent implements QueryCaller {

	protected static final String ERROR_SET_SQL_BEFORE_EXECUTE = "Before exequting query you have set sql statement";

	private static final String CLASS_NAME = CallablePersistent.class.getName();

	private static PreparedResultSetConverter resultSetConverter = new PreparedResultSetConverter();  

	private LogHelper log = LogHelper.getLog();
	
	private List<Object> params = new ArrayList<Object>(); 
	
	protected String sql;
	
	public void setSql(String sql) {
		
		this.sql = sql ;
	}

	@Override
	public void reset() {

		super.reset();
		
		this.sql = null ;
		params = new ArrayList<Object>();
	}
	
	public PreparedPersistent() {
		
		super();
		statementType = StatementType.QUERY;
		this.dataSourceName = ConfigHelper.getConfigManager().getDefaultJndiName();
	}

	public void setNull(int type) {

		params.add(new NullParam(type));
	}

	public void setParam(Object value) {

		params.add(value);
	}

	public void setParams(List<Object> params) {

		this.params = params;
	}

	public void setParams(Object[] params) {

		setParams(Arrays.asList(params));
	}

	public void setParam(int value) {

		params.add(new Integer(value));
	}

	public void setParam(long value) {

		params.add(new Long(value));
	}

	public void setParam(float value) {

		params.add(new Float(value));
	}

	public void setParam(double value) {

		params.add(new Double(value));
	}

	protected PreparedStatement createStatement() throws Exception {
		   
		   Connection conn = getConnection();
		   
			return conn.prepareCall(sql);   
	}

	public int executeUpdate() throws TechnicalException, SQLException {

			PreparedStatement stmnt = null;
			TechnicalException technicalException = null;

		try {
			// get the statement
			stmnt = prepareCall();

			// run the query
			int result = stmnt.executeUpdate();

			return result;
		} catch (TechnicalException e) {

			technicalException = new TechnicalException(CLASS_NAME, e.getMessage(), e);
	        throw technicalException;
	        
		} catch (SQLException e) {
			throw e;
		} finally {
			try {

				// close the statement
				if (stmnt != null) {
					stmnt.close();
				}

				closeConnection();

			} catch (SQLException e) {
	            throw (SQLException) ExceptionUtil.initCause(e, technicalException);            
			} catch (Exception e) {
	            Exception ex = (Exception) ExceptionUtil.initCause(e, technicalException);
	            throw new TechnicalException(CLASS_NAME, ex.getMessage(), ex);
			}
		}
	}

	public com.leaf.framework.jdbc.resultset.ResultSet executeQuery() throws TechnicalException, SQLException {

		PreparedStatement stmnt = null;
		ResultSet rs = null;
	    TechnicalException technicalException = null;

		try {
			// get the statement
			stmnt = prepareCall();

			// runs the query
			rs = stmnt.executeQuery();

			// send the result
			return new DefaultResultSet(resultSetConverter.convert(rs));
		
		} catch (Exception e) {
	        technicalException = new TechnicalException(CLASS_NAME, e.getMessage(), e);
			throw technicalException;
		} finally {
			
			try {
				// close the resultset
				if (rs != null) {
					rs.close();
				}

				// close the statement
				if (stmnt != null) {
					stmnt.close();
				}

				closeConnection();

			} catch (SQLException e) {
	            throw (SQLException) ExceptionUtil.initCause(e, technicalException);            
			} catch (Exception e) {
	            Exception ex = (Exception) ExceptionUtil.initCause(e, technicalException);
				throw new TechnicalException(CLASS_NAME, ex.getMessage(), ex);
			}
		}
	}
	
	public PreparedStatement prepareCall( ) throws TechnicalException {

		try {
			
			PreparedStatement statement = null;

			if (sql == null) {
				throw new TechnicalException(CLASS_NAME, ERROR_SET_SQL_BEFORE_EXECUTE);
			}

			statement = createStatement();
			
			String message = MessageFormat.format(DEBUG_QUERY, new Object[]{sql});
			log.debug(message);
			
			// add the parameters
			for (int i = 1; i <= params.size(); i++) {

				Object param = params.get(i - 1);

				String message1 = MessageFormat.format(DEBUG_PARAM, new Object[]{param});
				log.debug(message1);

				if (param == null) {
					statement.setString(i , null);
					
				} else if (param instanceof Boolean) {
					statement.setBoolean(i , ((Boolean) param).booleanValue());
					
				} else if (param instanceof byte[]) {

					ByteArrayInputStream stream = new ByteArrayInputStream((byte[]) param);
					statement.setBinaryStream(i, stream, stream.available());

				} else if (param instanceof InputStream) {
					statement.setBinaryStream(i , ((InputStream) param), ((InputStream) param).available());

				} else if (param instanceof Date) {
					statement.setDate(i , (Date) param);

				} else if (param instanceof Double) {
					statement.setDouble(i , ((Double) param).doubleValue());

				} else if (param instanceof Float) {
					statement.setFloat(i , ((Float) param).floatValue());

				} else if (param instanceof Integer) {
					statement.setInt(i , ((Integer) param).intValue());

				} else if (param instanceof Long) {
					statement.setLong(i , ((Long) param).longValue());

				} else if (param instanceof NullParam) {
					statement.setNull(i , ((NullParam) param).getType());

				} else if (param instanceof Short) {
					statement.setShort(i , ((Short) param).shortValue());

				} else if (param instanceof String) {
					statement.setString(i , (String) param);

				} else if (param instanceof Timestamp) {
					statement.setTimestamp(i , (Timestamp) param);

				} else if (param instanceof BigDecimal) {
					statement.setBigDecimal(i , (BigDecimal) param);
				} 
	            else { 
	            	String message2 = MessageFormat.format(ERROR_PARAMETER_NOT_SUPPORTED, new Object[]{param.getClass()});
	            	throw new TechnicalException(CLASS_NAME, message2);
	    		}
			}

			return statement;
			
		} catch (Exception e) {
			throw new TechnicalException(CLASS_NAME, e.getMessage(), e);
		}
	}
}
