package com.baselib.cache;

import com.baselib.bean.container.BeanHelper;


public class DefaultCacheWatchDog  implements CacheWatchDog
{
	
	public void refresh() {
		
		CacheManager  cacheManager = BeanHelper.getBean(CacheManager.class);
		
		cacheManager.refresh();
	}
	
}
	
