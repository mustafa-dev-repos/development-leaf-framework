package com.baselib.bean.lifecycle;

/**
 * 
 * 
 * @Auhtor: Mustafa 
 */

public class BeanLifeCycle {

	private boolean initialized;

	private long refreshRate;

	private long lastRefresh;
	
	/**
	 * @return the initialized
	 */
	public boolean isInitialized() {
		
		return initialized;
	}

	/**
	 * @param initialized the initialized to set
	 */
	public void setInitialized( boolean initialized ) {
		
		this.initialized = initialized;
	}

}
