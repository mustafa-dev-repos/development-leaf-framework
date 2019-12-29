package com.baselib.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import com.baselib.model.NameValuePair;

/**
 * 
 * @Auhtor: Mustafa   
 */

public final class PropertyUtil {

    private PropertyUtil() { }

    public static Object getAttribute(Object beanObject, String _attributeName) throws Exception {
		int pointIndex = _attributeName.indexOf(".");
		String attributeName = _attributeName;

		if (pointIndex > 0) {
			attributeName = attributeName.substring(0, pointIndex);
		}

		Object result = org.apache.commons.beanutils.PropertyUtils.getNestedProperty(beanObject, attributeName);

		if (pointIndex > 0) {
			return getAttribute(result, _attributeName.substring(pointIndex + 1, _attributeName.length()));
		}

		return result;
	}
    
    /**
     * Return the values of the specified properties of the specified bean.
     *
     */
    public static Object[] getProperties(Object bean, String[] names) 
    	throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        if (names == null || names.length == 0) {
            return new Object[0];
        }

        Object[] values = new Object[names.length];

        for (int i = 0; i < names.length; i++) {
            values[i] = org.apache.commons.beanutils.PropertyUtils.getProperty(bean, names[i]);
        }
        return values;
    }
    
    /**
     * set the values of the specified properties of the specified bean.
     *
     * @see org.apache.commons.beanutils.PropertyUtils#getProperty
     */
    public static void setProperties(Object bean, NameValuePair[] nameValue ) 
    	throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        if (nameValue == null || nameValue.length == 0) {
            return ;
        }

        for (int i = 0; i < nameValue.length; i++) {
             org.apache.commons.beanutils.PropertyUtils.setProperty(bean, nameValue[i].getName(), nameValue[i].getValue() );
        }
    }

    /**
     * Return the value of the specified array property of the specified bean, 
     * as an Object array
     *
     */
    public static Object[] getArrayProperty(Object bean, String name)
        throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        Object value = org.apache.commons.beanutils.PropertyUtils.getProperty(bean, name);
        if (value == null) {
            return null;
            
        } else if (value instanceof Collection) {
            return ((Collection) value).toArray();
            
        } else if (value.getClass().isArray()) {
            return (Object[]) value;
            
        } else {
            return new Object[] {value};
        }
    }
}