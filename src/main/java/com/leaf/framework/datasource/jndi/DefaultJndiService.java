package com.leaf.framework.datasource.jndi;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.baselib.log.LogHelper;
import com.leaf.framework.datasource.DefaultDataSourceHolder;
import com.leaf.framework.datasource.exception.DataSourceException;

/**
 * 
 * @Auhtor: Mustafa   
 */

public class DefaultJndiService implements JndiService {
		
		private static final String ERROR_NAMING_EXCEPTION = "NamingException for [{0}]: [{1}]";

		private static final String DEBUG_MESSAGE = "Adding to parameter from JNDI: [{0}]";

	    protected static final String JNDI_ENVIRONMENT = "java:comp/env";

	    protected static final String CLASS_NAME = DefaultJndiService.class.getName();

	    private static LogHelper log = LogHelper.getLog();

		public DefaultJndiService() {

			super( );
		}

	    public Map<String,DefaultDataSourceHolder> getDataSource()  {

	    		HashMap<String, DefaultDataSourceHolder> jndiParameters = new HashMap<String, DefaultDataSourceHolder>();
	    		String jndiParameterName = null;

	        try {

	        	InitialContext initialContext = new javax.naming.InitialContext();
	            NamingEnumeration enumeration = initialContext.list(JNDI_ENVIRONMENT);

	            Context rootContext = (Context) initialContext.lookup(JNDI_ENVIRONMENT);

	            if (enumeration != null) {
	            	
	                while (enumeration.hasMore()) {

	                	NameClassPair nameClassPair = (NameClassPair) enumeration.nextElement();

	                	jndiParameterName = nameClassPair.getName();

	                	Object jndiObject = rootContext.lookup(jndiParameterName);

	                    if (jndiObject instanceof DataSource) {

	                    	jndiParameters.put(jndiParameterName, new DefaultDataSourceHolder(jndiParameterName, (DataSource) jndiObject ));

	                    	String message = MessageFormat.format(DEBUG_MESSAGE, new Object[] {jndiParameterName});
	                    	log.debug(message);
	                    }
	                }
	                enumeration.close();
	            }
	        }
	         catch (NamingException e) {
	        	 
	            String message = MessageFormat.format(ERROR_NAMING_EXCEPTION, new Object[] {jndiParameterName, e.getMessage()});
	            log.error(message);
	        }

	        return jndiParameters;
	    }

		public DataSource getDataSource(String jndiName)  {

			try {	

				InitialContext initialContext = new javax.naming.InitialContext();
				return (DataSource)initialContext.lookup(jndiName);

			}
			catch (NamingException e) {

				String message = MessageFormat.format(ERROR_NAMING_EXCEPTION, new Object[] {jndiName, e.getMessage()});
				throw new DataSourceException(CLASS_NAME,message, e );
			}
		}
}