package com.leaf.framework.jdbc.util;

import java.text.MessageFormat;

import com.baselib.exception.TechnicalException;
import com.baselib.log.LogHelper;

/**
 * 
 * @Auhtor: Mustafa   
 */

public class ProcedureUtil {

	private static final String ERROR_PREPARING_STORE_PROCEDURE = "Error while preparing the stored procedure call string [{0}] | Query: [{1}]";

    private static final String ERROR_PREPARING_FUNCTION = "Error while preparing the stored function call string [{0}] | Query: [{1}]";

    private static LogHelper log = LogHelper.getLog();

    private static String CLASS_NAME = ProcedureUtil.class.getName() ; 
    
    public static String getProcCallString(String procedureName, int paramCount) throws TechnicalException {

        StringBuilder strProc = new StringBuilder();

        try {

        	strProc.append("{ call ");
            strProc.append(procedureName);

            strProc.append("(");

            for (int i = 0; i < (paramCount - 1); i++) {
                strProc.append("?,");
            }

            strProc.append("?) }");

            log.info(strProc.toString());

            return strProc.toString();

        } catch (Exception e) {

        	String message = MessageFormat.format(ERROR_PREPARING_STORE_PROCEDURE, new Object[] { procedureName, strProc });
            throw new TechnicalException(CLASS_NAME, message);
        }
    }

    public static String getFuncCallString(String functionName, int paramCount) throws TechnicalException {


        StringBuilder strFunc = new StringBuilder();

        try {

        	strFunc.append("{? = call ");
            strFunc.append(functionName);
            strFunc.append("(");

            for (int i = 0; i < (paramCount - 2); i++) {
                strFunc.append("?,");
            }

            if ((paramCount - 1) > 0) {
                strFunc.append("?");
            }
            
            strFunc.append(") }");

            // print call
            log.info(strFunc.toString());

            return strFunc.toString();
        } catch (Exception e) {

        	String message = MessageFormat.format(ERROR_PREPARING_FUNCTION, new Object[] { functionName, strFunc });
            throw new TechnicalException(CLASS_NAME, message);
        }
    }

}
