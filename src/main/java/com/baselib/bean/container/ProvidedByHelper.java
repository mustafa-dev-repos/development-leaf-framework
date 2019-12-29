package com.baselib.bean.container;

import java.text.MessageFormat;

import com.baselib.bean.ImplementationProvider;
import com.baselib.bean.metadata.BeanMetaData;
import com.baselib.exception.TechnicalException;
import com.baselib.util.ClassUtil;

/**
 * 
 * 
 * @Auhtor: Mustafa 
 */

public class ProvidedByHelper {
	
	private static final String CLASS_NAME = ProvidedByHelper.class.getName(); 

	private static final String ERROR_PROVIDER_RETURNED_NULL = "[{0}] provider class could not returned implementation class for [{1}] interface";

	@SuppressWarnings("unchecked")
	public static Class getImplClass(Class<?> clazz,BeanMetaData classDesc) {

		Class providerClass = classDesc.getProvidedByClass();

		ImplementationProvider provider = (ImplementationProvider) ClassUtil.newInstance(providerClass);

		// TODO use type casting here..		
		Class instance = provider.getImplClass();

		if (instance == null) {

			String message = MessageFormat.format(ERROR_PROVIDER_RETURNED_NULL, new Object[]{providerClass.getName(), clazz.getName() });
			throw new TechnicalException(CLASS_NAME, message);
		}

		return instance;
	}
	
}
