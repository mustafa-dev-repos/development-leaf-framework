package com.leaf.framework.transaction.interceptor;

import java.lang.reflect.Method;

import com.baselib.bean.container.BeanHelper;
import com.baselib.log.LogHelper;
import com.baselib.proxy.InterceptorContext;
import com.leaf.framework.context.Context;
import com.leaf.framework.transaction.annotation.Transactional;
import com.leaf.framework.transaction.manager.TransactionManager;

public class DefaultTransactionInterceptor implements TransactionInterceptor {

	private static final String ERROR_OPEN_TRANSACTION_DETECTED = "Open transaction detected. Coding error.";

	private static final String CLASS_NAME = DefaultTransactionInterceptor.class.getName();

	private TransactionManager previousTransaction = null;
	
	private TransactionManager transaction = null;

	private Context context = Context.getContext();

	protected static LogHelper log = LogHelper.getLog();

public final void beforeInvoke(InterceptorContext interceptorContext) throws Throwable {
	
	if (isRequiresNew(interceptorContext.getMethod())) {
		
		// get a new transaction manager..
		transaction = BeanHelper.getBean(TransactionManager.class);
		
		transaction.beginTransaction();

		// if there is already a started transaction backup it..
		TransactionManager existingTransaction = context.getTransactionManager();
		
		if (existingTransaction != null) {
			
			previousTransaction = existingTransaction;		
		}
			
		// set newly created transaction as global transaction..
		context.setTransactionManager(transaction);
		
	} else {

        if (context.getTransactionManager() == null) {

                   transaction = BeanHelper.getBean(TransactionManager.class);
                   
                   transaction.beginTransaction();
                   
                   context.setTransactionManager(transaction);
        }
	}
}

public final void afterInvoke(InterceptorContext interceptorContext)  throws Throwable {

	if (transaction != null) {
		
		transaction.commitTransaction();
	}
}

public final void afterThrowing(InterceptorContext interceptorContext) throws Throwable {
	
	if (transaction != null) {
		
		try {

			transaction.rollbackTransaction();
		} catch (Throwable tt) {

			log.error(CLASS_NAME, tt.getMessage() , tt);			
		}
		
	}
}

public final void afterFinally(InterceptorContext interceptorContext) {

	if (transaction != null) {
		
		if (previousTransaction != null) {

			context.setTransactionManager(previousTransaction);
			previousTransaction = null;
		}
			
		if (transaction.isInTransaction()) {
			
			log.error(CLASS_NAME, ERROR_OPEN_TRANSACTION_DETECTED, null);			
		}
		
		transaction = null;
	}
}

protected final boolean isRequiresNew(Method method) {

	// TODO cache method meta data information..
	Transactional transactional = method.getAnnotation(Transactional.class);

	if (transactional == null) {
		
		transactional = method.getClass().getAnnotation(Transactional.class);
	}
	
	if (transactional != null) {
		
		return transactional.requiresNew();
	}

	return false;

 }

}
