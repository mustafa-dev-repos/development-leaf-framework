package com.leaf.framework.transaction.interceptor;

import com.baselib.bean.Bean;
import com.baselib.module.system.SystemDefaultModule;
import com.baselib.proxy.InterceptorHandler;

@Bean(implementedBy = DefaultTransactionInterceptor.class, module = SystemDefaultModule.class, singleton = false)

public interface TransactionInterceptor extends InterceptorHandler {

}
