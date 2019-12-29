package com.baselib.module;

import com.baselib.bean.Bean;

// module type must be default modulee for all orphan beans..
@Bean(module = DefaultModule.class, singleton = false)

public interface DefaultBean {

}
