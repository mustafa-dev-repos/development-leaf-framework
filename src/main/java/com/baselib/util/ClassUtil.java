package com.baselib.util;

import java.util.HashSet;
import java.util.Set;

import com.baselib.exception.TechnicalException;
import com.baselib.log.LogHelper;

/**
 * 
 * @Auhtor: Mustafa   
 */

public class ClassUtil {

	private static String CLASS_NAME =  ClassUtil.class.getName();
	
	private static LogHelper log = LogHelper.getLog(); 

	private static Set<String>  singleValuedObjects;

	private static Set<String>  primitiTypeSet;
	
	
	static {

		singleValuedObjects = new HashSet<String>();

		singleValuedObjects.add("int");
		singleValuedObjects.add("java.lang.Integer");
		singleValuedObjects.add("float");
		singleValuedObjects.add("java.lang.Float");
		singleValuedObjects.add("long");
		singleValuedObjects.add("java.lang.Long");
		singleValuedObjects.add("boolean");
		singleValuedObjects.add("java.lang.Boolean");
		singleValuedObjects.add("byte");
		singleValuedObjects.add("java.lang.Byte");
		singleValuedObjects.add("short");
		singleValuedObjects.add("java.lang.Short");
		singleValuedObjects.add("double");
		singleValuedObjects.add("java.lang.Double");
		singleValuedObjects.add("char");
		singleValuedObjects.add("java.lang.Character");
		
		singleValuedObjects.add("java.lang.String");		
		singleValuedObjects.add("java.lang.BigDecimal");

		primitiTypeSet = new HashSet<String>();

		primitiTypeSet.add("int" + "java.lang.Integer");
		primitiTypeSet.add("float" + "java.lang.Float");
		primitiTypeSet.add("long" + "java.lang.Long");
		primitiTypeSet.add("boolean" + "java.lang.Boolean");
		primitiTypeSet.add("byte" + "java.lang.Byte");
		primitiTypeSet.add("short" + "java.lang.Short");
		primitiTypeSet.add("double" + "java.lang.Double");
		primitiTypeSet.add("char" + "java.lang.Character");
	}
	
	public static boolean isSingleValued(Class clazz) {

		if (singleValuedObjects.contains(clazz.getName())) {
			return true;
		}
		
		return false;
	}

	private ClassUtil() {
		super();
	}

	/**
	 * @return new instance of type clazz
	 */
	public static <T> T newInstance(Class<T> clazz) {
		
		try {
			
			return clazz.newInstance();

		} catch (InstantiationException e) {
			throw new TechnicalException(CLASS_NAME, clazz.getName() + " must have public and no-argument constructor", e);
		} catch (IllegalAccessException e) {
			throw new TechnicalException(CLASS_NAME, clazz.getName() + " should be public", e);
		}
	}

	public static Object newInstance(String classname) {
		
		try {
			
			return newInstance(Class.forName(classname));
			
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * This method calculates the class name of the clazz parameter
	 */
	public static String getName(Class clazz) {

		String name = clazz.getClass().getName();

		int index;

		if ((index = name.lastIndexOf('$')) != -1)
			return name.substring(index + 1);

		if ((index = name.lastIndexOf('.')) != -1)
			return name.substring(index + 1);

		return name;

	}

	public static String getName(Object obj) {
			return getName(obj.getClass());
	}

	/**
	 * @return true if the provided classname is in the classpath
	 */
	public static boolean isPresent(String className) {
		
		try {
			
			Class.forName(className);
			return true;
		} catch (NoClassDefFoundError e) {
			
			log.debug(className + " class found in the classpath");
			return false;
		} catch (ClassNotFoundException e) {
			
			log.debug(className + " class found in the classpath");
			return false;
		}
	}
	
    public static Class forName(String className) {

        try {

        	return Class.forName(className, true, Thread.currentThread().getContextClassLoader());
            
        } catch (ClassNotFoundException e) {
            try {
				return Class.forName(className);
			} catch (ClassNotFoundException e1) {

				throw new TechnicalException(e1);
			}
        }
    }

    public static ClassLoader getClassLoader(Class callingClass) {
    
    	ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    	if (classLoader == null) {
    		classLoader = callingClass.getClassLoader();
    	}
    
    	return classLoader;
    }

    public static boolean isAssignableFrom(Class<?> interfaceName, Object implObject) {
        
    	if (implObject == null ) return true;
    	
    	return isAssignableFrom(interfaceName, implObject.getClass());
    }
    
    public static boolean isAssignableFrom(Class<?> clazz1, Class<?> clazz2) {
        
    	if (clazz2 == null ) return false;
    	
        if (clazz1.isAssignableFrom(clazz2) ) {
        	return true;
        }
        	return primitiTypeSet.contains(clazz1.getName() + clazz2.getName());
	}
    
	public static Class getClass(String className) throws ClassNotFoundException {
//TODO replace with hashmap search..
		if (className.equals("int"))
			return Integer.class;

		if (className.equals("float"))
			return Float.class;

		if (className.equals("long"))
			return Long.class;

		if (className.equals("boolean"))
			return Boolean.class;

		if (className.equals("byte"))
			return Byte.class;

		if (className.equals("short"))
			return Short.class;

		if (className.equals("double"))
			return Double.class;

		if (className.equals("char"))
			return Character.class;

		return Class.forName(className);
	}    
}
