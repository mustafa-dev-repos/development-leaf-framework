package com.baselib.cache;

import com.baselib.bean.Bean;
import com.baselib.watchdog.WatchDog;
import com.baselib.watchdog.WatchDogType;

//TODO refactor like below 
//@WatchDogType(name = "Cache Manager Watch Dog", refreshRate = 5000 ,watchDogFor = CacheManager.class)

@WatchDogType(name = "Cache Manager Watch Dog", refreshRateInMs = 30000 )

@Bean(implementedBy= DefaultCacheWatchDog.class, singleton = true)

public interface CacheWatchDog extends WatchDog {

}
