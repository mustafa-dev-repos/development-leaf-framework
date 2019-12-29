package com.baselib.bean.container;

import java.text.MessageFormat;

import com.baselib.config.ConfigHelper;
import com.baselib.config.ConfigManager;
import com.baselib.exception.TechnicalException;
import com.baselib.phase.Phase;
import com.baselib.phase.PhaseHelper;
import com.baselib.util.ClassUtil;

/**
 *
 * 
 * @Auhtor: Mustafa 
 */

public abstract class BeanHelper {
	
	private static final String CLASS_NAME = BeanHelper.class.getName();
	
	private static final String ERROR_BEAN_MANAGER_CLASS_NOT_FOUND = "Bean manager class not found. [{0}] class is not available in the class path.";

	private static final String ERROR_IS_NOT_ASSIGNABLE = "[{0}] class is not assignable from [{1}].";

	private static final String ERROR_BEAN_MANAGER_NOT_INITIALIZED = "Bean manager is not successfuly initialized. Bean manager implementation information is null.[{0}] property is null. Check your bean manager config definitions. Current system level is : [{0}]";

	private static final String ERROR_BEAN_MANAGER_NOT_CONFIGURED_YET = "You can not use bean manager because of configuration of its configuration is not completed yet. Check your config type definitions. Current system level is : [{0}]";
	
	private static BeanManager beanManager ;
	
	static {
		
		initialize();
	}

	private static void initialize() {
		
		ConfigManager configManager = ConfigHelper.getConfigManager();
		
		String beanManagerClassName = configManager.getConfigProperty(BeanManager.class.getName());
		
		if (beanManagerClassName == null) {
			
			Phase level = PhaseHelper.getSystemLevel();
			
			if (level.geValue() <= Phase.BEAN_FACTORY_CONSTRUCTION.geValue()) {

				String message = MessageFormat.format(ERROR_BEAN_MANAGER_NOT_CONFIGURED_YET, new Object[] {level.name().toString() });
				
				throw new TechnicalException(CLASS_NAME, message);
			} 

			String message = MessageFormat.format(ERROR_BEAN_MANAGER_NOT_INITIALIZED, new Object[] { BeanManager.class.getName(), level.name().toString() });
			throw new TechnicalException(CLASS_NAME, message);
		
		}

		if (!ClassUtil.isPresent(beanManagerClassName)) {

			String message = MessageFormat.format(ERROR_BEAN_MANAGER_CLASS_NOT_FOUND, new Object[] { beanManagerClassName });
			throw new TechnicalException(CLASS_NAME, message);
		}
		
		Class<?> clazz = ClassUtil.forName(beanManagerClassName);
		
		if (!BeanManager.class.isAssignableFrom(clazz)) {

			String message = MessageFormat.format(ERROR_IS_NOT_ASSIGNABLE, new Object[] { beanManagerClassName , BeanManager.class.getName() });
			throw new TechnicalException(CLASS_NAME, message);
		}
		
		Class<? extends BeanManager> implClass =  (Class<? extends BeanManager>) clazz;
		
		beanManager = ClassUtil.newInstance(  implClass);
	}
	
	public static <T> T getBean(Class<T> clazz) {

		return beanManager.getBean(clazz);
	}

	// TODO CHECK warnings
	public static Object getBean(String beanName) {

		if (ClassUtil.isPresent(beanName)) {

			return (Object) getBean(ClassUtil.forName(beanName));
		}

		// TODO : make parametrik this message..
		throw new TechnicalException("No class definition found  for " + beanName);

	}
	
}
