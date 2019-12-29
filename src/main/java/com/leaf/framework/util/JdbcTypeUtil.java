package com.leaf.framework.util;

import java.sql.Types;

import com.baselib.exception.TechnicalException;

/**
 * 
 * @Auhtor: Mustafa   
 */

public class JdbcTypeUtil {

	//TODO Check with oracle to java type mappings

	public static int getJdbcType(Class clazz) {

		// convert this into map ans search from mappp
		if ( clazz.getName().equals("java.lang.String")) {
			return Types.VARCHAR;
		}

		if (clazz.getName().equals("int") || clazz.getName().equals("java.lang.Integer")) {
			return Types.INTEGER;
		}

		if (clazz.getName().equals("double") || clazz.getName().equals("java.lang.Double")) {
			return Types.DOUBLE;
		}
		
		if (clazz.getName().equals("long") || clazz.getName().equals("java.lang.Long")) {
			return Types.BIGINT;
		}

		if ( clazz.getName().equals("java.math.BigDecimal")) {
			return Types.DECIMAL;
		}

		if (clazz.getName().equals("short") || clazz.getName().equals("java.lang.Short")) {
			return Types.SMALLINT;
		}

		if (clazz.getName().equals("float") || clazz.getName().equals("java.lang.Float")) {
			return Types.FLOAT;
		}
		
		if (clazz.getName().equals("boolean") || clazz.getName().equals("java.lang.Boolean")) {
			return Types.BOOLEAN;
		}

		if (clazz.getName().equals("byte") || clazz.getName().equals("java.lang.Byte")) {
			return Types.BIT;
		}

		if (clazz.getName().equals("char") || clazz.getName().equals("java.lang.Character")) {
			return Types.CHAR;
		}
		
		//TODO make parametrik here
		throw new TechnicalException("Java to Jdbc type mappig error for " + clazz.getName() );

	}
}
