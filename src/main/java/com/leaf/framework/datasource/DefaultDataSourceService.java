package com.leaf.framework.datasource;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.baselib.bean.container.BeanHelper;
import com.baselib.config.ConfigManager;
import com.leaf.framework.datasource.exception.DataSourceException;
import com.leaf.framework.datasource.jndi.JndiService;
import com.leaf.framework.jdbc.exception.ConnectionException;

/**
 * 
 * @Auhtor: Mustafa   
 */

public class DefaultDataSourceService implements DataSourceService {

	private static final String ERROR_JNDI_NOT_FOUND = "[{0}] data source could not found. Check your jndi datasource definitions.";
	
	private static DataSourceService dsConnectionPool ;
	
    private static Map<String,DefaultDataSourceHolder> jndiMap = new HashMap<String, DefaultDataSourceHolder>()  ;

    private static String defaultDataSourceName = null;

    static {

    	System.out.println("default jndi" +BeanHelper.getBean(ConfigManager.class).getDefaultJndiName()); 

    }
    public DefaultDataSourceService()
    {
    }
    
	public void initialize() {
		
    	if (dsConnectionPool == null) {
        	
        	synchronized (DataSourceService.class) {

        		if (dsConnectionPool != null) 
        			return;
        		
        		JndiService reader = BeanHelper.getBean( JndiService.class );

        		jndiMap = reader.getDataSource();
        		
        		setDefaultSourceName();
        		
        		dsConnectionPool = new DefaultDataSourceService();

        		DataSourceService.class.notifyAll();
        	}
        }
		
	}

	public static DataSourceService getInstance()
    {


        	return dsConnectionPool;
    }

    public final DataSource getDataSource(String jndiName) throws DataSourceException, ConnectionException
    {

    	if (jndiName == null) {
    		
    		jndiName = defaultDataSourceName ;
    	}
    	
    	DefaultDataSourceHolder dsHolder = (DefaultDataSourceHolder) jndiMap.get(jndiName);

    	if (dsHolder == null)
        {

    		JndiService reader = BeanHelper.getBean(JndiService.class);

    		DataSource dataSource = reader.getDataSource( jndiName );
    		
    		if (dataSource != null ) {

    			setDataSource(jndiName, dataSource);
    			return dataSource;
    		}
        }
    	
    	if (dsHolder == null)
        {
            String  message = MessageFormat.format(ERROR_JNDI_NOT_FOUND, new Object[]{ jndiName == null ? "jndi name is NULL" : jndiName });
            
        	throw new DataSourceException( message );
        }

        return dsHolder.getDataSource();
    }

	private synchronized void setDataSource(String jndiName,DataSource dataSource)
    {
			if (!isDataSourceAvailable( jndiName )) {
				
				 jndiMap.put(jndiName, new DefaultDataSourceHolder(jndiName, dataSource));
			}
    }

	private boolean isDataSourceAvailable(String jndiName)
    {
			return jndiMap.containsKey(jndiName);

    }
	
    private static void setDefaultSourceName() {
    	
    		// if there is only one datasource set as default datasource.. 
    		if (jndiMap != null && jndiMap.keySet().size() == 1 ) {
    			
    			defaultDataSourceName = jndiMap.get( jndiMap.keySet().iterator().next()).getJndiName() ;
    		}
    		
    		//TODO:  get from external config file. ConfigParamHelper.	DEFAULT_DATASOURCE_NAME
    }
	
}
