package com.leaf.framework.dab.converter;
import java.sql.CallableStatement;import java.util.Map;import com.leaf.framework.dab.annotation.Procedure;
/** *   * @author Mustafa  */
public interface ResultConverter {
	/**	 * 	 * Converts executed callable statement data into java objects	 * 	 */	public Object convert(CallableStatement callableStatement, Procedure procedure, Map values) throws Exception;	
}
