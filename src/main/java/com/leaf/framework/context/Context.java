package com.leaf.framework.context;

import com.baselib.bean.Bean;
import com.baselib.bean.container.BeanHelper;
import com.baselib.module.system.SystemDefaultModule;
import com.leaf.framework.transaction.manager.TransactionManager;

/**
 * 
 * @Auhtor: Mustafa   
 */

// important : Context class must not be accessed by framework classes, 
//             Because context is initialized by request coming from presentation server..  

@Bean(implementedBy = DefaultContext.class, module = SystemDefaultModule.class, singleton = true)

public abstract class Context {
	
	private TransactionManager transactionManager;

	private static ThreadLocal<Context> context = new ThreadLocal<Context>();
	
	/**
	 * @return the deepFishContext
	 */
	public static Context getContext() {
		
		Context ctx = context.get();

		//TODO : remove this initializing.. Service broker must set business Context..
		
		if (ctx == null) {

			ctx = BeanHelper.getBean(Context.class);
			
			ctx.setTransactionManager( null );

			setContext(ctx);
		}

		return ctx;
	}

	/**
	 * @param deepFishContext the deepFishContext to set
	 */
	protected static void setContext(Context businessContext) {
		
		context.set( businessContext );
	}

	public static final void releaseContext() {
		
		// set context data to default not applicable state	
		// contextThreadLocal.set( ShellFishContext.getDestroyedRequestData() );
		// TODO: close all resources like transaction manager
		
		context.set( null );
	}

	/**
	 * @param transactionManager the transactionManager to set
	 */
	public void setTransactionManager(TransactionManager transactionManager) {
		
		this.transactionManager = transactionManager;
	}

	/**
	 * @return the transactionManager
	 */
	public TransactionManager getTransactionManager() {
		
		return transactionManager;
	}

}
