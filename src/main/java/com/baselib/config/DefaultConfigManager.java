package com.baselib.config;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 
 * @Auhtor: Mustafa 
 */

class DefaultConfigManager implements ConfigManager {

	private Map<String,String> propertyMap = new HashMap<String,String>();
	
	private String defaultJndiName = null;

	/**
	 * @return the defaultJndiName
	 */
	public String getDefaultJndiName() {
		return defaultJndiName;
	}

	/**
	 * @param defaultJndiName the defaultJndiName to set
	 */
	public void setDefaultJndiName(String defaultJndiName) {
		this.defaultJndiName = defaultJndiName;
	}

	public void setConfigProperty(String propertyName, String propertyValue) {

		propertyMap.put(propertyName, propertyValue);
	}

	public String getConfigProperty(String propertyName) {
		
		return propertyMap.get(propertyName);
	}

}
