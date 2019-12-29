package com.baselib.proxy;

import com.baselib.bean.Bean;
import com.baselib.cache.DefaultCacheInterceptorHandler;

@Bean(implementedBy = DefaultCacheInterceptorHandler.class, singleton = true)
public interface CacheInterceptorHandler extends InterceptorHandler {

}
