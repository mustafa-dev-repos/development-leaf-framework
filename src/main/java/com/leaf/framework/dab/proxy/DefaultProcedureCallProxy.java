package com.leaf.framework.dab.proxy;

import java.lang.reflect.Method;

import com.baselib.bean.Bean;
import com.baselib.module.system.SystemDefaultModule;
import com.baselib.util.StringUtil;
import com.leaf.framework.dab.annotation.Procedure;
import com.leaf.framework.dab.annotation.SQLCallerInterface;
import com.leaf.framework.dab.cache.SQLInterceptorCacheManager;
import com.leaf.framework.dab.call.CallType;
import com.leaf.framework.dab.context.DefaultProcedureContext;
import com.leaf.framework.dab.exception.SQLInvocationException;
import com.leaf.framework.dab.handler.SQLCallHandler;
import com.leaf.framework.dab.handler.SQLCallHandlerFactory;
import com.leaf.framework.dab.interceptor.SQLInterceptor;
import com.leaf.framework.proxy.BaseImplementationHandler;

/**
 * 
 * @Auhtor: Mustafa   
 */

//TODO try to make singleton this class..

@Bean(module = SystemDefaultModule.class, singleton = false)

public class DefaultProcedureCallProxy extends BaseImplementationHandler implements ProcedureCallProxy {
    
    private String defaultPackagePrefix;

    private Class<?> interfaceClass;
 
    public void initialize(Class<?> interfaceClass) {

    	this.interfaceClass = interfaceClass;
    	
    	SQLCallerInterface spInterface = interfaceClass.getAnnotation(SQLCallerInterface.class);

        if (spInterface != null) {

        	this.defaultPackagePrefix = StringUtil.isEmpty(spInterface.prefix()) ? null : spInterface.prefix();
        	
        } else {

        	this.defaultPackagePrefix = "";
        }
    }

    
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        
    	CallType callType = null;
    	
     try {

    	Procedure storedProcedure = method.getAnnotation( Procedure.class );
    	
    	if (storedProcedure != null) {

    		callType = CallType.STORED_PROCEDURE;	
    	}
    	else {
    		//TODO Check for QUery and set operation type.. 	
    	}
    	
    	DefaultProcedureContext sqlContext = new DefaultProcedureContext( method, storedProcedure, args, defaultPackagePrefix);  
    	
        SQLInterceptor interceptor = getInvocationInterceptor( sqlContext );
	 
        if (interceptor != null)
        interceptor.beforeInvoke( sqlContext );
        
        SQLCallHandler sqlCaller = SQLCallHandlerFactory.getCallHandler( callType );
        
        Object callResult = sqlCaller.handleCall( sqlContext );

        if (interceptor != null)
        interceptor.afterInvoke( sqlContext );
        
        return callResult;
     }
     finally {
    	 
     }
    }

	private SQLInterceptor getInvocationInterceptor(DefaultProcedureContext context) {

		if (context.getStoredProcedure() == null)
			return null;
		
		SQLInterceptor interceptor = (SQLInterceptor) SQLInterceptorCacheManager.getClassFromCache(context.getStoredProcedure().invocationInterceptor());

		if (interceptor == null) {

			interceptor = createInvocationInterceptor(context.getStoredProcedure());
		}

		return interceptor;
	}

	//TODO check synronized.. put class level lock on this class.

	private synchronized SQLInterceptor createInvocationInterceptor(Procedure procedure) {

		SQLInterceptor interceptor = (SQLInterceptor) SQLInterceptorCacheManager.getClassFromCache(procedure.invocationInterceptor());

		if (interceptor == null) {
		
			try {
			
				interceptor = (SQLInterceptor) procedure.invocationInterceptor().newInstance();

				SQLInterceptorCacheManager.putClassToCache( interceptor );

			} catch (Exception e) {

				throw new SQLInvocationException("Procedure invocation interceptor class creating error.", e);

			}
		}
		
		return interceptor;
		
	}
}

