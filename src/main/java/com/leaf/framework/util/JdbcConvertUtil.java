package com.leaf.framework.util;

import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.baselib.util.ReflectUtil;

/**
 * 
 * @Auhtor: Mustafa   
 */

public class JdbcConvertUtil {

    private static final String ERROR_CANNOT_CONVERT_DOUBLE = "Can not convert to double number.";

    private static final String ERROR_CANNOT_CONVERT_INT = "Can not convert to int number.";

    private static final String ERROR_CANNOT_CONVERT_TIME = "Can not convert to Time.";

    private static final String ERROR_CANNOT_CONVERT_DATE = "Can not convert to date";

    private static final String ERROR_CANNOT_CONVERT_BYTE_ARRAY = "Can not convert to byte array";
    
    private static final String ERROR_CANNOT_CONVERT_BOOLEAN = "Can not convert to boolean";

    private static final String ERROR_CANNOT_CONVERT_TIMESTAMP = "Can not convert to timestamp";

    private static final String ERROR_CANNOT_CONVERT_BIGDECIMAL = "Can not convert to bigDecimal";
    
    private static final String ERROR_CANNOT_CONVERT_SHORT = "Can not convert to short number.";

    private static final String ERROR_CANNOT_CONVERT_BINARY = "Can not convert to binaryStream.";

    private static final String ERROR_CANNOT_CONVERT_FLOAT = "Can not convert to float number.";

    private static final String ERROR_CANNOT_CONVERT_BYTE = "Can not convert to byte number.";

    private static final String ERROR_CANNOT_CONVERT_BLOB = "Can not convert to blob.";

    private static final String ERROR_CANNOT_CONVERT_CLOB = "Can not convert to clob.";
    
    public static long convertToLong(Object obj) throws SQLException {

    	try {
            return ((Number) obj).longValue();
        }
        catch (NullPointerException e) {
            return 0;
        }
        catch (Exception e) {
            throw new SQLException(ERROR_CANNOT_CONVERT_INT);
        }
	}

	public static int convertToInt(Object obj) throws SQLException {

		try {
            return ((Number) obj).intValue();
        }
        catch (NullPointerException e) {
            return 0;
        }
        catch (Exception e) {
            throw new SQLException(ERROR_CANNOT_CONVERT_INT);
        }
	}

    public static  double convertToDouble(Object obj) throws SQLException {

		try {
            return ((Number) obj).doubleValue();
        }
        catch (NullPointerException e) {
            return (double) 0.0F;
        }
        catch (Exception e) {
            throw new SQLException(ERROR_CANNOT_CONVERT_DOUBLE);
        }
	}

	public static String convertToString(Object obj) {

		String result = null;
		
		if (obj != null) {
			result = obj.toString();
		}
	
		return result;
	}

	public static java.math.BigDecimal convertToBigDecimal(Object obj) throws SQLException  {

	try {

		if (obj instanceof BigDecimal){
			
		    return (BigDecimal) obj;
		    
		} else {
			
		    try {
		        return new java.math.BigDecimal(((Number) obj).doubleValue());
		    }
		    catch (NullPointerException e) {
		        return null;
		    }
		    
		}
	 }
	catch (Throwable t) {
			throw new SQLException(ERROR_CANNOT_CONVERT_BIGDECIMAL);
   }

	}


	public static java.sql.Time getTime(Object obj) throws SQLException {

		try {

            if (obj instanceof java.sql.Time) {
                return (java.sql.Time) obj;
            } else {
                try {
                    return new java.sql.Time(((java.util.Date) obj).getTime());
                }
                 catch (NullPointerException ne) {
                    return null;
                }
            }
        }
         catch (Throwable t) {
            throw new SQLException(ERROR_CANNOT_CONVERT_TIME);
        }
	}

	public static byte[] convertToBytes(Object obj) throws SQLException {

		try {
            return (byte[]) (obj);
        }
         catch (Exception e) {
            throw new SQLException(ERROR_CANNOT_CONVERT_BYTE_ARRAY);
        }
	}
	
	public static boolean convertToBoolean(Object obj) throws SQLException {

		try {
            if (obj == null) {
                return false;
            }
            return ((Boolean) obj).booleanValue();
        }
         catch (ClassCastException cce) {
            throw new SQLException(ERROR_CANNOT_CONVERT_BOOLEAN);
        }
	}
	
	public static short convertToShort(Object obj) throws SQLException {
		try {
            return ((Number) obj).shortValue();
        }
         catch (NullPointerException e) {
            return 0;
        }
         catch (Exception e) {
            throw new SQLException(ERROR_CANNOT_CONVERT_SHORT);
        }
	}

	public static byte convertToByte(Object obj) throws SQLException {

		try {
            return ((Number) obj).byteValue();
        }
         catch (NullPointerException e) {
            return (byte) 0;
        }
         catch (Exception e) {
            throw new SQLException(ERROR_CANNOT_CONVERT_BYTE);
        }
	}

	public static float convertToFloat(Object obj) throws SQLException {

		try {
            return ((Number) obj).floatValue();
        }
         catch (NullPointerException e) {
            return 0.0F;
        }
         catch (Exception e) {
            throw new SQLException(ERROR_CANNOT_CONVERT_FLOAT);
        }
	}

	public static java.io.InputStream convertToBinaryStream(Object obj) throws SQLException {

		try {
            return (java.io.InputStream) (obj);
        }
        catch (Exception e) {
            throw new SQLException(ERROR_CANNOT_CONVERT_BINARY);
        }
	}

	public static java.sql.Blob convertToBlob(Object obj) throws SQLException {

		try {
			return (java.sql.Blob) obj;
			
		} catch (Exception e) {
			throw new SQLException(ERROR_CANNOT_CONVERT_BLOB);
		}
	}
				  
	public static java.sql.Date convertToDate(Object obj) throws SQLException {

						try {

					        if(obj == null)
					               return null;
					        
				            if ("oracle.sql.DATE".equals(obj.getClass().getName())){  

				            	try {
				            		
				                    return (java.sql.Date) ReflectUtil.invokeMethod(obj, "dateValue");

				            	} catch (Throwable e) {
				                } 
				            }
					        
				            if (obj instanceof java.sql.Date)
				                return (java.sql.Date) obj;
				            else{
				                try {
				                    return new java.sql.Date(((java.util.Date) obj).getTime());
				                } catch (NullPointerException ne) {
				                    return null;
				                }
				            }
				        }
				         catch (Exception e) {
				            throw new SQLException(ERROR_CANNOT_CONVERT_DATE);
				        }
	}

	public static Timestamp convertToTimestamp(Object obj) throws SQLException {

		try {

            if(obj == null)
               return null;

            if ("oracle.sql.TIMESTAMP".equals(obj.getClass().getName())) {  

            	try {
            		
                    return (Timestamp) ReflectUtil.invokeMethod(obj, "timestampValue");
                    
            	} catch (Throwable e) {
                }
            	
            }
            if (obj instanceof Timestamp ) {
            	
                return (Timestamp) obj;
                
            }else {
            	
                try {
                    return new Timestamp(((java.util.Date) obj).getTime());
                }
                catch (NullPointerException ne) {
                    return null;
                }
            }
        }
         catch (Throwable t) {
            throw new SQLException(ERROR_CANNOT_CONVERT_TIMESTAMP);
        }
	}
	
    public static Clob convertToClob(Object obj) throws SQLException {

    	try {
			return (Clob) obj;
			
		} catch (Exception e) {
			throw new SQLException(ERROR_CANNOT_CONVERT_CLOB);
		}

  }
	
	
}
