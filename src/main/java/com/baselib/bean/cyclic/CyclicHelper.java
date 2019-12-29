package com.baselib.bean.cyclic;

/**
 * 
 * @Auhtor: Mustafa 
 * 
 */

public class CyclicHelper {

	private static ThreadLocal<BeanCreationInfo> threadClassInfo = new ThreadLocal<BeanCreationInfo>(); 

	public static BeanCreationInfo getBeanCreationInfo() {
		
		BeanCreationInfo beanCreateInfo = threadClassInfo.get();
		
		if (beanCreateInfo == null ) {
			
			beanCreateInfo = new BeanCreationInfo();
			
			threadClassInfo.set(beanCreateInfo);
		}

		return beanCreateInfo;
	}

}

