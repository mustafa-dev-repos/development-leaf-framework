package com.baselib.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.baselib.exception.TechnicalException;

public class CollectionUtil {

	 public Class getCollectionType(final Class collectionType) {
	        Class collectionClass = null;
	        if (collectionType.isArray()) {
	            collectionClass = Array.class;
	        } else if (Map.class.isAssignableFrom(collectionType)) {
	            collectionClass = Map.class;
	        } else if (Collection.class.isAssignableFrom(collectionType)) {
	            collectionClass = Collection.class;
	        }
	        return collectionClass;
	    }

	    public Class getArrayValueType(final Class collectionType) {
	        if (collectionType.isArray()) {
	            return collectionType.getComponentType();
	        }
	        return null;
	    }
	
public static boolean isCollection(Class collectionType){

	if (Array.class.isAssignableFrom(collectionType)) {
         return true;
     } else if (Map.class.isAssignableFrom(collectionType)) {
         return true;
     } else if (Collection.class.isAssignableFrom(collectionType)) {
         return true;
     } else {
         return false;
     }
}
@SuppressWarnings("unchecked")
public static Object getInstance(Class collectionType, int size){

	if (Array.class.isAssignableFrom(collectionType)) {
         return getArrayInstance(collectionType, size);
     } else if (Map.class.isAssignableFrom(collectionType)) {
         return getMapInstance(collectionType);
     } else if (Collection.class.isAssignableFrom(collectionType)) {
         return getCollectionInstance(collectionType);
     } else {
         return false;
     }
}

private static Object[] getArrayInstance(final Class collectionType, int size)
{

	final Object[] result = (Object[])Array.newInstance(collectionType.getComponentType(), size);
	return result;
}

private static Collection getCollectionInstance(final Class<? extends Collection> collectionClass)
{

	Class<? extends Collection> collectionType = collectionClass;

	if (collectionType.isInterface()) {

	if (List.class.isAssignableFrom(collectionType)) {
		collectionType = ArrayList.class;
	} else if (Set.class.isAssignableFrom(collectionType)) {
		collectionType = HashSet.class;
	} else if (Collection.class.isAssignableFrom(collectionType)) {
		collectionType = ArrayList.class;
	} else if (BlockingQueue.class.isAssignableFrom(collectionType)) {
		collectionType = ArrayBlockingQueue.class;
	} else if (Queue.class.isAssignableFrom(collectionType)) {
		collectionType = LinkedList.class;
	} else if (SortedSet.class.isAssignableFrom(collectionType)) {
		collectionType = TreeSet.class;
	}
	}
	
	try {
		Collection result = collectionType.newInstance();
		return result;
	} catch (InstantiationException e) {
		throw new TechnicalException(e);
	} catch (IllegalAccessException e) {
		throw new TechnicalException(e);
}
}


private static Map getMapInstance(final Class<? extends Map> expectedType)
{
    Class<? extends Map> collectionType = expectedType;

    if (collectionType.isInterface()) {

        if (SortedMap.class.isAssignableFrom(collectionType)) {
            collectionType = TreeMap.class;
        } else if (ConcurrentMap.class.isAssignableFrom(collectionType)) {
            collectionType = ConcurrentHashMap.class;
        } else if (Map.class.isAssignableFrom(collectionType)) {
            collectionType = HashMap.class;
        }
    }
    
    try {
    
    	Map result = collectionType.newInstance();
        return result;
    } catch (InstantiationException e) {
        throw new TechnicalException(e);
    } catch (IllegalAccessException e) {
        throw new TechnicalException(e);

    }
}
}
