package com.baselib.util;

import java.io.InputStream;

public class ResourceUtil {

    public static InputStream getResourceAsStream(String resourceName) {

    	resourceName = cutLeadingSlash(resourceName);

        return ClassUtil.getClassLoader(ResourceUtil.class).getResourceAsStream(resourceName);
    }

    private static String cutLeadingSlash(String resourceName) {

    	if (resourceName.startsWith("/"))
            resourceName = resourceName.substring(1);
        return resourceName;
    }
	
}
