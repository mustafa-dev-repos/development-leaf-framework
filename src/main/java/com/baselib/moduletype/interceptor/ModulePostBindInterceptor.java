package com.baselib.moduletype.interceptor;

import com.baselib.bean.Bean;
import com.baselib.module.system.SystemDefaultModule;
import com.baselib.moduletype.Module;

@Bean(implementedBy= DefaultModulePostBindInterceptor.class, module= SystemDefaultModule.class, singleton = true) 

public interface ModulePostBindInterceptor {

	public void handle(Module module);
}
