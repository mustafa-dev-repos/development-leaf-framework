package com.leaf.framework.datasource;

import com.baselib.bean.Bean;
import com.baselib.module.system.SystemDefaultModule;

@Bean(implementedBy = DefaultDataSourceHolder.class, module = SystemDefaultModule.class, singleton = false)

public interface DataSourceHolder {

}
