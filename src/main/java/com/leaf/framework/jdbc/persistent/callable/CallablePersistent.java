package com.leaf.framework.jdbc.persistent.callable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baselib.config.ConfigHelper;
import com.baselib.exception.TechnicalException;
import com.baselib.log.LogHelper;
import com.baselib.util.ExceptionUtil;
import com.baselib.util.StringUtil;
import com.leaf.framework.jdbc.converter.CallableStatementConverter;
import com.leaf.framework.jdbc.param.InOutParam;
import com.leaf.framework.jdbc.param.NullParam;
import com.leaf.framework.jdbc.param.OutParam;
import com.leaf.framework.jdbc.param.ParamHolderHelper;
import com.leaf.framework.jdbc.param.ProcedureParamHolder;
import com.leaf.framework.jdbc.persistent.BasePersistent;
import com.leaf.framework.jdbc.resultset.ResultSet;
import com.leaf.framework.jdbc.statement.StatementType;
import com.leaf.framework.jdbc.util.ProcedureUtil;
import com.leaf.framework.util.JdbcConvertUtil;

/**
 * 
 * @Auhtor: Mustafa   
**/

public class CallablePersistent extends BasePersistent implements ProcedureCaller, OutParamAware  {

	protected static final String ERROR_SET_PROCEDURE_NAME = "You have to set procedure or function name before executing procedure call";

	private static final String CLASS_NAME = CallablePersistent.class.getName();

	private LogHelper log = LogHelper.getLog();

	private ProcedureParamHolder params = ParamHolderHelper.getProcedureParamHolder();

	private String procedureName = null ; 

	boolean isProcedure = true;
	
	public CallablePersistent() {
		super();

		statementType = StatementType.PROCEDURE;
		
		super.dataSourceName = ConfigHelper.getConfigManager().getDefaultJndiName();
	}

	public void setProcedureName(String procName) {
		
		
		this.procedureName = procName ;
		isProcedure = true ;
	}

	public void setFunctionName(String functionName) {
		
		this.procedureName = functionName ;
		isProcedure = false ;
	}

	/**
	 * @param params the params to set
	 */
	public void setParams(ProcedureParamHolder params) {
		this.params = params;
	}

	/**
	 * @param params the params to set
	 */

	public OutParamAware setNull(int type) {

		params.add(new NullParam(type));

		return (OutParamAware) this;
	}

	public OutParamAware setParam(Object value) {

		params.add(value);

		return (OutParamAware) this;
	}

	public void setParams(List<Object> paramList) {
		
//remove this metod		params.addAll(paramList) ;
	}

	public void setParams(Object[] params) {

		setParams(Arrays.asList(params));
	}

	public OutParamAware setParam(int value) {

		params.add(new Integer(value));
		return (OutParamAware) this;
	}

	public OutParamAware setParam(long value) {

		params.add(new Long(value));
		return (OutParamAware) this;
	}

	public OutParamAware setParam(float value) {

		params.add(new Float(value));
		return (OutParamAware) this;
	}

	public OutParamAware setParam(double value) {

		params.add(new Double(value));
		return (OutParamAware) this;
	}

	public void registerOutParam(int typeofOutParameter) {

		params.addOutParam(typeofOutParameter);
	}

	public void registerOutParam(int typeofOutParameter, String typeName)  {

		params.addOutParam(typeofOutParameter, typeName) ;
	}

	public void registerAsOutParam(int typeofOutParameter)  {
		params.bindOutToInParam(typeofOutParameter);
	}

	public void registerAsOutParam(int typeofOutParameter, String typeName) {

		params.bindOutToInParam(typeofOutParameter, typeName);
	}

	protected CallableStatement createStatement(String sql) throws Exception {
		   
		   Connection conn = getConnection();
			return conn.prepareCall(sql);   
	}
	
	//TODO refactor this method into super class..	

	public CallableStatement prepareCall( ) throws TechnicalException {

		CallableStatement statement = null;

		try {

			if (StringUtil.isEmpty(procedureName))
				throw new TechnicalException(CLASS_NAME, ERROR_SET_PROCEDURE_NAME);
			
			String sql = (isProcedure ? ProcedureUtil.getProcCallString(procedureName, params.getParamCount() ) : ProcedureUtil.getFuncCallString(procedureName, params.getParamCount()));

			statement = createStatement(sql);
			
			String message = MessageFormat.format(DEBUG_QUERY, new Object[]{sql});
			log.debug(message);

			Object param = null;
			InOutParam inOutParam = null;			
			
			// add the parameters
			for (int i = 1; i <= params.getParamCount(); i++){

				param = params.get(i - 1);
				
				if (param instanceof InOutParam) {

					inOutParam = (InOutParam) param;
					
					// extract input value from parameter	
					param = inOutParam.getInputValue();
	            	statement.registerOutParameter(i, inOutParam.getReturnType() );
				}

				String message1 = MessageFormat.format(DEBUG_PARAM, new Object[]{param});
				log.debug(message1);

				if (param == null) {
					statement.setString(i , null);

				} else if (param instanceof String) {
					statement.setString(i , (String) param);

				} else if (param instanceof Integer) {
					statement.setInt(i , ((Integer) param).intValue());

				} else if (param instanceof Long) {
					statement.setLong(i , ((Long) param).longValue());

				} else if (param instanceof Date) {
					statement.setDate(i , (Date) param);

				} else if (param instanceof Double) {
					statement.setDouble(i , ((Double) param).doubleValue());

				} else if (param instanceof Float) {
					statement.setFloat(i , ((Float) param).floatValue());

				} else if (param instanceof NullParam) {
					statement.setNull(i , ((NullParam) param).getType());

				} else if (param instanceof Short) {
					statement.setShort(i , ((Short) param).shortValue());

				} else if (param instanceof Timestamp) {
					statement.setTimestamp(i , (Timestamp) param);

				} else if (param instanceof BigDecimal) {
					statement.setBigDecimal(i , (BigDecimal) param);
				} else if (param instanceof OutParam) {
	            	//TODO make here type name aware..
	            	((CallableStatement) statement).registerOutParameter(i, ((OutParam)param).getReturnType() );		
				
	            }else if (param instanceof Boolean) {
	            	statement.setBoolean(i , ((Boolean) param).booleanValue());

	            } else if (param instanceof byte[]) {
					ByteArrayInputStream stream = new ByteArrayInputStream((byte[]) param);
					statement.setBinaryStream(i, stream, stream.available());

				} else if (param instanceof InputStream) {
					statement.setBinaryStream(i , ((InputStream) param), ((InputStream) param).available());
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

    public ResultSet executeQuery() throws TechnicalException, SQLException {
    	
    	throw new TechnicalException(CLASS_NAME, "This method not implemented yet.. deepfish.. todo.");
    }

	public int executeUpdate() throws TechnicalException, SQLException {

		CallableStatement stmnt = null;
		TechnicalException technicalException = null;

	try {
		// get the statement
		stmnt = prepareCall();
		
		// run the query
		int result = stmnt.executeUpdate();

		returnedObjects = CallableStatementConverter.convert(stmnt, params.getOutParams());
		
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

/*************************************************************************************
 * Output Results..
 *
 *************************************************************************************/	
	
	Map<Integer, Object> returnedObjects = null;
	
	public BigDecimal getBigDecimal(int loc) throws SQLException {

		return JdbcConvertUtil.convertToBigDecimal(returnedObjects.get(loc)) ;
	}

	public double getDouble(int loc) throws SQLException {

		return JdbcConvertUtil.convertToDouble(returnedObjects.get(loc)) ;
	}

	public int getInt(int loc) throws SQLException {

		return JdbcConvertUtil.convertToInt(returnedObjects.get(loc)) ;
	}

	public long getLong(int loc) throws SQLException {

		return JdbcConvertUtil.convertToLong(returnedObjects.get(loc)) ;
	}

	public String getLongString(int loc) throws SQLException, IOException {
		// TODO handle laterrr ehuu
		return null;
	}

	public String getString(int loc) throws SQLException {

		return JdbcConvertUtil.convertToString(returnedObjects.get(loc)) ;
	}

	public Timestamp getTimeStamp(int loc) throws SQLException {

		return JdbcConvertUtil.convertToTimestamp(returnedObjects.get(loc)) ;
	}

	public String getClob(int loc) throws SQLException {

		Clob clobValue = JdbcConvertUtil.convertToClob(returnedObjects.get(loc));

	    if (clobValue != null) {
	 
	        return clobValue.getSubString(1, (int) clobValue.length());
	    }

	    return null;
	}
	
	public Object getObject(int loc) {

		return returnedObjects.get(loc);
	}
	
	@Override
	public void reset() {

		super.reset();

		params = ParamHolderHelper.getProcedureParamHolder();
		isProcedure = true;
	}

	/**
	 * @return the returnedObjects
	 */
	public Map<Integer, Object> getReturnedObjects() {
		return returnedObjects;
	}


	
	
}
