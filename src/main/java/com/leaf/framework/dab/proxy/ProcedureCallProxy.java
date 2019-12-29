package com.leaf.framework.dab.proxy;

import com.baselib.bean.Bean;
import com.baselib.module.system.SystemDefaultModule;
import com.baselib.proxy.ImplementationHandler;

@Bean(implementedBy = DefaultProcedureCallProxy.class, module= SystemDefaultModule.class, singleton = true)

public interface ProcedureCallProxy extends ImplementationHandler {

}
