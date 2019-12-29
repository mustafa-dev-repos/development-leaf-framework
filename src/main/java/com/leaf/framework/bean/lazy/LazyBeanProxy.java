package com.leaf.framework.bean.lazy;

import com.baselib.bean.Bean;
import com.baselib.module.system.SystemDefaultModule;
import com.baselib.proxy.ImplementationHandler;

/**
 * 
 * @Auhtor: Mustafa 
 */

@Bean(implementedBy=DefaultLazyBeanProxy.class, module= SystemDefaultModule.class, singleton=false)

public interface LazyBeanProxy extends ImplementationHandler {

}
