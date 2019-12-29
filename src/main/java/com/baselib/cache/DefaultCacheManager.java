package com.baselib.cache;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.baselib.log.LogHelper;

/**
 * 
 * @Auhtor: Mustafa 
 */

public class DefaultCacheManager implements CacheManager
{
	private static final String KEY_SEPARATOR = ":";

	private static final String ARGUMENT_SEPARATOR = "^";

	private static final String ERROR_REFRESH_FAILED = "Exception occured during cache refresh. Exception :  [{0}]";

	private static final String DEBUG_REMOVED_ITEM_COUNT = "[{0}] item(s) removed from cache";
	
	private LogHelper log = LogHelper.getLog();

	private Map<String, CachedObject> cache;

	/**
	 * Constructor.
	 */
	
	public DefaultCacheManager()
	{
		
		super();
		
		this.cache = new ConcurrentHashMap<String, CachedObject>();

	}

	/**
	 * Add an element to the cache manager. If the key already exists for the sub cache name, does nothing.
	 * @param category the sub cache name
	 * @param key the key of what have to be cached
	 * @param value the value to be cached
	 */
	public void addElement(String key, Object value)
	{
		addElement(key, value, EXPIRATION_TIME_NEVER);
	}

	/**
	 * Add or update an element to the cache manager.
	 * @param category the sub cache name
	 * @param key the key of what have to be cached
	 * @param value the value to be cached
	 * @param expirationTime expiration time in millisecond. Could be EXPIRATION_TIME_NEVER.
	 */
	public void addElement(String key, Object value, long expirationTime)
	{

		cache.put(key, new CachedObject(value, expirationTime));
	}

	/**Return a cached element from its sub cache name and its key.
	 * @param category the sub cache name
	 * @param key the key of what have to be cached
	 * @return the element
	 */
	public Object getElement(String key)
	{
		CachedObject cachedObject = (CachedObject) cache.get(key);
		
		if (cachedObject == null || cachedObject.isExpired())
		{
			return null;
		}
		return cachedObject.getCachedObject();
	}

	/**
	 * reset the cache for a given sub cache name.
	 * @param category
	 */
	public void remove(String key)
	{
		cache.remove(key);
	}

	/**
	 * remove a cached element from its sub cache name and its key.
	 * @param category the sub cache name
	 * @param key the key of what have to be cached
	 */
	public void removeElement( String key)
	{
		cache.remove( key);
	}

	public void refresh()
	{
		int itemsRemoved = 0 ;	
	    try
  		{

	    	for (Iterator it = cache.keySet().iterator(); it.hasNext();)
			{
				String key = (String) it.next();
				
				CachedObject cachedObject = (CachedObject) cache.get(key);

				if (cachedObject.isExpired() || (cachedObject.getCachedObject() == null))
				{
					
					cache.remove(key);
					itemsRemoved++;
				}
			}

			log.debug(MessageFormat.format(DEBUG_REMOVED_ITEM_COUNT, new Object[] { itemsRemoved } ));
			
  		}
	    
		catch (Throwable e) {
			
	        String message = MessageFormat.format(ERROR_REFRESH_FAILED, new Object[] { e.getMessage() } );
			log.error(message);
		}
		
	}

    public String createKeyForCache(String interfaceClassName, Method method, Object[] methodArguments) {

        StringBuilder sb = new StringBuilder();

        boolean argumentAdded = false;
        
        sb.append(interfaceClassName);
        sb.append(KEY_SEPARATOR);
        sb.append(method.getName());
        
        if (methodArguments != null) {
        	
            sb.append("[");
            
            for (int i = 0; i < methodArguments.length; i++) {
            	
            	if (argumentAdded)
            		sb.append(ARGUMENT_SEPARATOR);
            		
                Object object = methodArguments[i];
                
                if (object != null) {
                	sb.append(object.toString());
                	argumentAdded = true;
                }
                else {
                	
                	argumentAdded = false;
                }
                
            }
            
            sb.append("]");
        }
        
        return sb.toString();
    }

	public void clearCache() {
		
		cache.clear();	
	}	
}
