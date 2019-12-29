package com.baselib.bean.binder;

import com.baselib.bean.Bean;
import com.baselib.module.system.SystemDefaultModule;

@Bean(implementedBy = DefaultBinder.class, module= SystemDefaultModule.class, singleton = true )

public interface Binder {

	public DefaultBindTo bind(Class<?> clazz);

}
