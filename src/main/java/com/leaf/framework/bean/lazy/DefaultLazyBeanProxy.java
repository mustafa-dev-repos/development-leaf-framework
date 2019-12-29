package com.leaf.framework.bean.lazy;

import java.lang.reflect.Method;

import com.baselib.bean.container.BeanHelper;
import com.leaf.framework.proxy.BaseImplementationHandler;

/**
 * 
 * @Auhtor: Mustafa 
 */


public class DefaultLazyBeanProxy extends BaseImplementationHandler implements LazyBeanProxy {

	private Object targetBean = null; 

	private Class<?> interfaceClass;

    public DefaultLazyBeanProxy() {
    	
    	super();
    }
	
	@Override
	public void initialize(Class<?> interfaceClass) {
		
		super.initialize(interfaceClass);
		
		this.interfaceClass = interfaceClass;
	}

	@Override    
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		
		if (targetBean == null) {
			//lazy initialization of object..
			targetBean = BeanHelper.getBean(this.interfaceClass) ;
		}

		return method.invoke(targetBean, args);

	}

}
