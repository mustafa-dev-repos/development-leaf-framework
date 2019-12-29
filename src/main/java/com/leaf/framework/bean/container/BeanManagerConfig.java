package com.leaf.framework.bean.container;

import com.baselib.bean.container.BeanManager;
import com.baselib.beantype.BeanFactoryType;
import com.baselib.config.Config;
import com.baselib.config.ConfigManager;

/**
 * 
 * @Auhtor: Mustafa 
 */


@BeanFactoryType( order = -100 )

public class BeanManagerConfig implements Config  {

	public void config(ConfigManager configManager) {

		configManager.setConfigProperty(BeanManager.class.getName(), DefaultBeanManager.class.getName());
		
	}

}
