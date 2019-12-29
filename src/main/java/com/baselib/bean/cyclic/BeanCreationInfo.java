package com.baselib.bean.cyclic;

import java.text.MessageFormat;
import java.util.LinkedList;

import com.baselib.exception.TechnicalException;

/**
 * 
 * @Auhtor: Mustafa 
 * 
 */

public class BeanCreationInfo {
	
	private static String CLASS_NAME = BeanCreationInfo.class.getName(); 

	private static final String ERROR_UNEXPECTED_EXCEPTION = "Unexpected exception occured during the cyclic dependency management of [{0}]. Coding error.";

	private static final String ERROR_CYCLE_DETECTED = "Cyclic dependency detected for [{0}]. Calling of [{1}] from [{2}] creates cyclic dependeny.";

	LinkedList<String> callGraph = new LinkedList<String>();
	
	public boolean isWaitingForInstance(String clazzName) {
		
		if (callGraph.size() == 0)
			return false;
			
		return callGraph.contains(clazzName);
		
	}

	public void checkWaitingForInstance(String clazzName) {

		if (isWaitingForInstance(clazzName)) {
			
			String message = MessageFormat.format(ERROR_CYCLE_DETECTED, new Object[] { clazzName, clazzName, callGraph.getLast()});
			
			throw new TechnicalException(CLASS_NAME, message);
		}
	}

	public void setWaitingInstanceFor(String clazzName) {
		
			if (isWaitingForInstance(clazzName)) {
			
				String message = MessageFormat.format(ERROR_CYCLE_DETECTED, new Object[] { clazzName, clazzName, callGraph.getLast()});
				throw new TechnicalException(CLASS_NAME, message);
			}

			callGraph.addLast(clazzName);
	}
	
	public void releaseWaitingInstanceFor(String clazzName) {
		
		// it must be last record
		
		if (callGraph.size() > 0 ) {
			
			if (clazzName.equals(callGraph.getLast()) ) {
				
				callGraph.removeLast();
			}
			else 
			{
				String message = MessageFormat.format(ERROR_UNEXPECTED_EXCEPTION, new Object[] { clazzName });
				throw new TechnicalException(CLASS_NAME, message);
			}
		}
	}

}
