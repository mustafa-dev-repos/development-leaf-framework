package com.baselib.config;

import com.baselib.bean.Bean;

/**
 * 
 * 
 * @Auhtor: Mustafa 
 */
@Bean(implementedBy= DefaultConfigManager.class, singleton = true)

public interface ConfigManager {

	public void setConfigProperty(String propertyName, String  propertyValue);

	public String getConfigProperty(String propertyName);

	public String getDefaultJndiName();
	
	public void setDefaultJndiName(String defaultJndiName);

}
