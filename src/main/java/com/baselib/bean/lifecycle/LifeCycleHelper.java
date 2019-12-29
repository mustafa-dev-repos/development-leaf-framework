package com.baselib.bean.lifecycle;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.baselib.bean.container.BeanHelper;
import com.baselib.bean.container.SingletonHelper;
import com.baselib.bean.metadata.AnnotationHelper;
import com.baselib.bean.metadata.BeanMetaData;
import com.baselib.bean.metadata.BeanMetaDataHelper;
import com.baselib.exception.TechnicalException;
import com.baselib.log.LogHelper;
import com.baselib.util.ClassUtil;
import com.baselib.util.ReflectUtil;

/**
 * 
 * @Auhtor: Mustafa 
 */
//TODO refactpr as bean.. remove static..
public class LifeCycleHelper {

	private static String CLASS_NAME = LifeCycleHelper.class.getName(); 

	private static final String WARN_INITIALIZATION_FAILED = "Initialization exception occured during call of [{0}] method. Exception : [{1}]";
	
	private static final String WARN_REFRESH_FAILED = "Refresh exception occured during call of [{0}] method. Exception : [{1}]";

	private static LogHelper log = LogHelper.getLog();

	public static void initialize(Object instance, BeanMetaData classDesc) {

		synchronized (classDesc) {
			
			if (classDesc.isInitialized())
				
				return;
		
			try {

				ReflectUtil.invokeMethod(instance, classDesc.getInitializeMethodName());
			
				classDesc.setInitialized(true);
			
			} catch (Throwable e) {
			
				log.warn(MessageFormat.format( WARN_INITIALIZATION_FAILED, new Object[]{instance.getClass().getName() + ":" + classDesc.getInitializeMethodName() + "()" , e.getMessage() }));
			
				log.error(e.getMessage(),e);
				
			}
		}
	}
	
	public static void refresh() { 

		try {

			Set<String> refreshEnableBeanSet = AnnotationHelper.getClassList(Refresh.class);
			
			if (refreshEnableBeanSet == null) return;

			String className  = null;

			boolean isRefreshNeeded ;	
			Object instance  = null;
			BeanMetaData metaData = null;
			
			for (Iterator<String> iter = refreshEnableBeanSet.iterator() ; iter.hasNext(); ) {

				try {

					className = iter.next();
					instance = SingletonHelper.getObject(className);
					
					if (instance == null ) continue;

					metaData = BeanMetaDataHelper.getClassDescriptor(ClassUtil.getClass(className));
					
					if (metaData.isRefreshEnabled()&& metaData.isRefreshNeeded()) {
					
						//by default all refresh methods has to be called periodically ..
						isRefreshNeeded = true;
					
						if (metaData.getRefreshInterceptor() != null) {
						
							RefreshInterceptor interceptor = BeanHelper.getBean(metaData.getRefreshInterceptor());
							isRefreshNeeded = interceptor.isRefreshNeeded();
						}
						
						if (isRefreshNeeded) {
							
							ReflectUtil.invokeMethod(instance , metaData.getRefreshMethodName() );
							metaData.setLastRefreshTime(System.currentTimeMillis());
						}
					}
				} 
				catch (Throwable e) {
					
					log.warn(MessageFormat.format(WARN_REFRESH_FAILED, new Object[]{instance == null ? "Class Not Set" : instance.getClass().getName() + ":" + metaData == null ? "" : metaData.getRefreshMethodName() + "()" , e.getMessage() }));
					
					log.error(e.getMessage(),e);
					
				}
			}
		} catch (Throwable e) {

			throw new TechnicalException(CLASS_NAME, e.getMessage(),e);
		}

	}
	
	public static class RefreshInfo {

		private Object instance;
		
		private String refreshMethod;
		private Class<? extends RefreshInterceptor> refreshInterceptor;
		
		public RefreshInfo(Object instance,String refreshMethod,Class<? extends RefreshInterceptor> refreshInterceptor ) {
			
			this.instance = instance;
			this.refreshMethod = refreshMethod;
			this.refreshInterceptor = refreshInterceptor;
			
		}

		/**
		 * @return the instance
		 */
		public Object getInstance() {
			return instance;
		}

		/**
		 * @return the refreshMethod
		 */
		public String getRefreshMethod() {
			return refreshMethod;
		}
		public Class<? extends RefreshInterceptor> getRefreshInterceptor() {
			return refreshInterceptor;
		}

	} 
}
