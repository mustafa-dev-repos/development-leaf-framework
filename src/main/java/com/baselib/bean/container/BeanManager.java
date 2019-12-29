package com.baselib.bean.container;

/**
 * 
 * @Auhtor: Mustafa 
 *   
 */

public interface BeanManager {

	   public abstract <T> T getBean(Class<T> clazz);

	   public Object getBean(String beanName);

}
