package com.baselib.util;

import java.lang.reflect.Method;

/**
 * 
 * @Auhtor: Mustafa   
 */


public class MethodUtil {

	public static int INVOCATION_LEVEL = 5;

	private MethodUtil() {
		super();
	}

	public static boolean isReturnVoid(Method method)
    {

		if (method.getReturnType() == void.class || 
			method.getReturnType() == Void.class) {
			
			return true;

		}
			return false;
	}
		
	public static  Method getInvokingMethod(Object params[])
    {
        Method method = null;
        try
        {
            String methodName = getInvokingElement().getMethodName();
            Class clazz = Class.forName(getInvokingElement().getClassName());
            Method methods[] = sun.reflect.misc.MethodUtil.getMethods(clazz);
            Method arr$[] = methods;
            int len = arr$.length;
            int i = 0;
            do
            {
                if(i >= len)
                    break;
                Method m = arr$[i];
                if(m.getName().equals(methodName) )
                {
                    method = m;
                    break;
                }
                i++;
            } while(true);
        }
        catch(ClassNotFoundException e)
        {
            throw new RuntimeException("Exception finding calling class", e);
        }
        return method;
    }

    public static StackTraceElement getInvokingElement()
    {

    	StackTraceElement elements[] = Thread.currentThread().getStackTrace();

        if(elements.length <= 0)
            return null;
        if(elements.length < INVOCATION_LEVEL)
            return null;
        else
            return elements[INVOCATION_LEVEL];
    }

    

    
}
