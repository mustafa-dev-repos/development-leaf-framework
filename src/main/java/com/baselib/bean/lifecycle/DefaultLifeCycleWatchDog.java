package com.baselib.bean.lifecycle;

/**
 * 
 * 
 * @Auhtor: Mustafa 
 */

public class DefaultLifeCycleWatchDog implements LifeCycleWatchDog {

	public void refresh() {
		
		LifeCycleHelper.refresh();

	}

}
