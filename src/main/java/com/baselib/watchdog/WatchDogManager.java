package com.baselib.watchdog;

import com.baselib.bean.Bean;

/**
 * 
 * @Auhtor: Mustafa   
 */

@Bean(implementedBy = DefaultWatchDogManager.class, singleton = false)

public interface WatchDogManager extends Runnable {

	  public void start(WatchDogType watchDogType, WatchDog watchDog);
	   
}
