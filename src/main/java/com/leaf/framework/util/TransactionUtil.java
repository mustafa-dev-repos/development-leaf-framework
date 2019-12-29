package com.leaf.framework.util;

import com.baselib.exception.TechnicalException;
import com.leaf.framework.transaction.manager.TransactionManager;

public class TransactionUtil {

	public static final String ERROR_ROLLBACK_NOT_BEGUN = "Trying to rollback a transaction that was not begun.";

	public static final String ERROR_COMMIT_TRANSACTION_NOT_BEGUN = "Trying to commit a transaction that was not begun.";

	private static final String CLASS_NAME = TransactionUtil.class.getName();
	
	public static boolean checkRollable(TransactionManager manager) {
		
		if (manager == null ) 
			return false;

    	if (!manager.isInTransaction()) {
            throw new TechnicalException(CLASS_NAME , ERROR_ROLLBACK_NOT_BEGUN);
        }

        if (!manager.isConnectionAvailable()) {
            return false;
        }
        
        	return true;
	}
	
	public static boolean checkCommitable(TransactionManager manager) {
		
		if (manager == null ) 
			return false;

        if (!manager.isInTransaction()) {
        	
            throw new TechnicalException(CLASS_NAME, ERROR_COMMIT_TRANSACTION_NOT_BEGUN);
        }

        if (!manager.isConnectionAvailable() ) {
            return false;
        }
        
        	return true;
	}

}
