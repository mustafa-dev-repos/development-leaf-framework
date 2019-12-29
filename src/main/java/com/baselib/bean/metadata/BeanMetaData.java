package com.baselib.bean.metadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;

import com.baselib.bean.Bean;
import com.baselib.bean.DefaultImplementedBy;
import com.baselib.bean.DefaultProvidedBy;
import com.baselib.bean.ImplementationProvider;
import com.baselib.bean.lifecycle.DefaultRefreshInterceptor;
import com.baselib.bean.lifecycle.Initialize;
import com.baselib.bean.lifecycle.Refresh;
import com.baselib.bean.lifecycle.RefreshInterceptor;
import com.baselib.cache.Cache;
import com.baselib.exception.TechnicalException;
import com.baselib.log.LogHelper;
import com.baselib.util.ClassUtil;

/**
 * 
 * @Auhtor: Mustafa   
 */

public class BeanMetaData {

    private static final String ERROR_METHOD_TAKES_ARGUMENT = "[{0}] method of [{1}] class takes argument. You can only use [{2}] with parameterless methods.";

    private static LogHelper log = LogHelper.getLog();
    
	private Class<?> clazz; 

	private boolean isCached;
	private boolean isInterface;
	private boolean isSingleton;
	private boolean isBean;
	private Bean beanAnnotation;
	
//TODO in the case of refresh initialization informatino must be kept..
//or in the case of instance refresh beanmetadata must be cleared.

	private boolean initialized = false;
	
	private String initializeMethodName;
	private boolean initializeEnabled;
	private long initializationTime = 0 ;
	private boolean isLoadOnStart;
	private String refreshMethodName;
	
	private Class<? extends RefreshInterceptor> refreshInterceptor;
	private boolean refreshEnabled;
	private long refreshRateInMs;	
	private long lastRefreshTime = 0;
	
	private boolean isAbstract;

	private boolean isImplementedBy;
	private Class<?> implementedBy;
	private String implementedByRemote;
	
	private boolean isProvidedBy;
	private Class<? extends ImplementationProvider> providedBy;
	private String providedByRemote;

	/**
	 * @return the isCached
	 */
	protected boolean isCached() {
		
		return isCached;
	}

	/**
	 * @return the implementedByAnnotation
	 */
	public Class getImplementedByClass() {

		if (implementedBy != null)
			return implementedBy;

		if (implementedByRemote != null)
			return ClassUtil.forName(implementedByRemote) ;

			return null;
	}

	/**
	 * 
	 * @return the providedByAnnotation
	 */
	public Class getProvidedByClass() {

		if (providedBy != null)
			return providedBy;
		 
		if (providedByRemote != null) {
			
			Class clazz = ClassUtil.forName( providedByRemote );

			if (!ImplementationProvider.class.isAssignableFrom(clazz)) {
				//TODO refactor here.. 	
				throw new TechnicalException("Provided by class has to implement ImplementationProvider");
			}

			return clazz;
		}
			
			return null;
	}

	/**
	 * @return the transactionalAnnotation
	 */
	
	public BeanMetaData(Class<?> clazz) {

		super();

		this.clazz = clazz;
		
		if (AnnotationHelper.isUsingAnnotation(clazz, Initialize.class)) {

			processInitialize();
		}

		if (AnnotationHelper.isUsingAnnotation(clazz, Refresh.class)) {

			processResfresh();
		}
		
		int modifiers = clazz.getModifiers();

		if (Modifier.isInterface(modifiers))
			
			isInterface = true;
		else 
			isInterface = false;
				
		if (Modifier.isAbstract(modifiers))
			
			isAbstract = true;
		else 
			isAbstract = false;
			
		beanAnnotation = clazz.getAnnotation(Bean.class); 
		
		if (beanAnnotation != null) {
			
			isBean = true;
			
			isSingleton = beanAnnotation.singleton();

			implementedBy = beanAnnotation.implementedBy();
			
			implementedByRemote = beanAnnotation.implementedByRemote();

			if (implementedBy != DefaultImplementedBy.class || !"".equals(implementedByRemote) ) {
				
 				isImplementedBy = true;
			}
			else {
				
				isImplementedBy = false;
			}

			providedBy = beanAnnotation.providedBy();
			
			providedByRemote = beanAnnotation.providedByRemote();
			
			if (providedBy != DefaultProvidedBy.class || !"".equals(providedByRemote)) {
				
				isProvidedBy = true;
			}
			else {
				
				isProvidedBy = false;
			}
		}
		
		isCached = AnnotationHelper.isUsingAnnotation(clazz, Cache.class); 
	}

    private void processInitialize() {

    	Method[] methods = clazz.getMethods();

    	for (int i = 0; i < methods.length; i++) {
        
    		Method method = methods[i];
            
    		if (method.isAnnotationPresent(Initialize.class)) {
    			
				Class[] params = method.getParameterTypes();
                
				if (params.length == 0) {

	    			initializeEnabled = true;
	    			isLoadOnStart =  method.getAnnotation(Initialize.class).loadOnStartup();
	    			initializeMethodName = method.getName();
	    			return;
				}
			    else {
			    
					String message = MessageFormat.format(ERROR_METHOD_TAKES_ARGUMENT, new Object[]{method.getName(), clazz.getName(), Initialize.class.getName() });
					log.error(message);	
				}
            }
        }

    }

    private void processResfresh() {

    	Method[] methods = clazz.getMethods();

    	for (int i = 0; i < methods.length; i++) {
        
    		Method method = methods[i];
    		
    		Refresh refreshAnnotation = method.getAnnotation(Refresh.class);
            
    		if (refreshAnnotation != null ) {
    			
				Class[] params = method.getParameterTypes();
                
				if (params.length == 0) {

	    			refreshEnabled = true;
	    			
	    			refreshMethodName = method.getName();
	    			refreshRateInMs = refreshAnnotation.refreshRateInMs();
	    			
	    			if (refreshAnnotation.refreshInterceptor() != DefaultRefreshInterceptor.class ) {
	    				
	    				refreshInterceptor = refreshAnnotation.refreshInterceptor();
	    			}
	    			
	    			return;
				}
			    else {
			    
					String message = MessageFormat.format(ERROR_METHOD_TAKES_ARGUMENT, new Object[]{method.getName(), clazz.getName(), Refresh.class.getName() });
					log.error(message);	
				}
            }
        }

    }
    
	/**
	 * @return the isInterface
	 */
	public boolean isInterface() {
		
		return isInterface;
	}

	/**
	 * @return the isProvidedBy
	 */
	public boolean isProvidedBy() {
		
		return isProvidedBy;
	}

	/**
	 * @return the isService
	 */
	public boolean isService() {
		
		return isBean;
	}

	/**
	 * @return the isSingleton
	 */
	public boolean isSingleton() {
		
		return isSingleton;
	}

	/**
	 * @return the isImplementedBy
	 */
	public boolean isImplementedBy() {
		return isImplementedBy;
	}

	/**
	 * @return the isAbstract
	 */
	public boolean isAbstract() {
		return isAbstract;
	}

	/**
	 * @return the serviceAnnotation
	 */
	protected Bean getBeanAnnotation() {
		return beanAnnotation;
	}
//TODO refactor this method for the well known annotation into separate method for the performance..
	public boolean isUsingAnnotation(Class<? extends Annotation> annotation) 
	{

//TODO cache result for later use..
		return AnnotationHelper.isUsingAnnotation(clazz, annotation);
	}

	/**
	 * @return the initializeEnabled
	 */
	public boolean isInitializeEnabled() {
		return initializeEnabled;
	}

	public boolean isLoadOnStart() {
		return isLoadOnStart;
	}
	
	/**
	 * @return the refreshEnabled
	 */
	public boolean isRefreshEnabled() {
		return refreshEnabled;
	}

	/**
	 * @return the initialized
	 */
	public boolean isInitialized() {
		return initialized;
	}

	/**
	 * @return the implementedBy
	 */
	public Class<?> getImplementedBy() {
		return implementedBy;
	}

	/**
	 * @param implementedBy the implementedBy to set
	 */
	public void setImplementedBy(Class<?> implementedBy) {
		this.implementedBy = implementedBy;
	}

	/**
	 * @param initialized the initialized to set
	 */
	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
		this.initializationTime = System.currentTimeMillis();
	}

	/**
	 * @return the initializeMethodName
	 */
	public String getInitializeMethodName() {
		return initializeMethodName;
	}

	/**
	 * @return the refreshMethodName
	 */
	public String getRefreshMethodName() {
		return refreshMethodName;
	}

	/**
	 * @return the clazz
	 */
	public Class<?> getMetaDataClass() {
		return clazz;
	}

	/**
	 * @return the refreshInterceptor
	 */
	public Class<? extends RefreshInterceptor> getRefreshInterceptor() {
		return refreshInterceptor;
	}

	/**
	 * @return the lastRefreshTime
	 */
	public long getLastRefreshTime() {
		return lastRefreshTime == 0 ? initializationTime : lastRefreshTime;
	}

	/**
	 * @param lastRefreshTime the lastRefreshTime to set
	 */
	public void setLastRefreshTime(long lastRefreshTime) {
		this.lastRefreshTime = lastRefreshTime;
	}

	public boolean isRefreshNeeded() {

		if (System.currentTimeMillis() - getLastRefreshTime() >= refreshRateInMs)
		
			return true;
		
			return false;
	}

}
