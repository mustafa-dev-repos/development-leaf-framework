package com.leaf.framework.transaction.manager;

import java.text.MessageFormat;

import com.baselib.bean.container.BeanHelper;
import com.baselib.log.LogHelper;
import com.leaf.framework.context.Context;

/**
 * 
 * @Auhtor: Mustafa   
 */


public class GlobalTransactionHelper {
	
	private static final String ERROR_CANNOT_ROLLBACK_GLOBAL_TRANSACTION = "Error occured during rollback of global transaction";

	private static LogHelper log = LogHelper.getLog();
	
	private static String CLASS_NAME = GlobalTransactionHelper.class.getName(); 


	public static boolean isInTransaction() {
			
		TransactionManager transactionManager = Context.getContext().getTransactionManager();
		
		if (transactionManager != null) {
			
			return transactionManager.isInTransaction();
		}
		
			return false;
		
	}

	public static TransactionManager getTransactionManager() {

		return Context.getContext().getTransactionManager();
		
	}

}
