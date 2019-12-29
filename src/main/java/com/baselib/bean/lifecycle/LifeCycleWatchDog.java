package com.baselib.bean.lifecycle;

import com.baselib.bean.Bean;
import com.baselib.watchdog.WatchDog;
import com.baselib.watchdog.WatchDogType;

@WatchDogType(name = "Life Cycle Watch Dog", refreshRateInMs = 10000)

@Bean(implementedBy = DefaultLifeCycleWatchDog.class, singleton = false )

public interface LifeCycleWatchDog extends WatchDog {

}
