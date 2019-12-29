package com.baselib.util;

import org.apache.commons.logging.LogFactory;

/**
 * 
 * @Auhtor: Mustafa   
 */

public class ExceptionUtil {

	    private ExceptionUtil() {}

	    public static void log(String pSender, String pErrorMessage, Throwable pThrowable) {

	    	LogFactory.getLog(pSender).error(pErrorMessage, pThrowable);
	    }

	    /**
	     * Log the message and the bound throwable
	     *
	     * @param   pSender        the full class name where the exception was thrown
	     * @param   pErrorMessage  the exception message
	     */
	    public static void log(String pSender, String pErrorMessage) {
	        log(pSender, pErrorMessage, null);
	    }

	    public static Throwable initCause(final Throwable source, final Throwable cause) {
	 
	    		Throwable rootCause = source.getCause();

	    		if (rootCause == null) {
		            source.initCause(cause);
		        } else {
		            initCause(rootCause, cause);
		        }
		        return source;
		    
	    }

	}
