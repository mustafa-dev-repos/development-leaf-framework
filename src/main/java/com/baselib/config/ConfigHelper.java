package com.baselib.config;

import com.baselib.bean.container.SingletonHelper;

/**
 * 
 * 
 * @Auhtor: Mustafa 
 */

public class ConfigHelper {
	
	// it must be created only using new  
	private static ConfigManager configManager ;

	static {
		
		configManager =  new DefaultConfigManager();
		SingletonHelper.setObject(ConfigManager.class, configManager);
		
	}

	/**
	 * @return the configManager
	 */
	public static ConfigManager getConfigManager() {
		return configManager;
	} 
	

}
