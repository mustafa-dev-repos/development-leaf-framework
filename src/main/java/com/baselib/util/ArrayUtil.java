package com.baselib.util;

import java.lang.reflect.Array;

/**
 * 
 * @Auhtor: Mustafa   
 */

public final class ArrayUtil {


    private ArrayUtil() { 		
    	super();
    }
    
    /**
     * Append an object at the end of an array. The size of the array is
     * incremented by one. The type of the concatened array is the same as the
     * array specified in argument
     * 
     * @param array Array to witch append an element
     * @param element Element to append
     * @return New array
     * @throws IllegalArgumentException if element's Class is not assignable
     * from array's component type
     */
    public static Object[] append(Object[] array, Object element) 
        throws IllegalArgumentException {
        if (array == null) {
            if (element == null) {
                return new Object[0];
            }
            return new Object[] {element};
        }
        
        if (element != null
         && !array.getClass().getComponentType().isAssignableFrom(element.getClass())) {
            throw new IllegalArgumentException(
                element.getClass().getName() + " is not assignable from " + array.getClass().getName());
        }
        
        Object[] newArray = 
            (Object[]) Array.newInstance(
                array.getClass().getComponentType(),
                array.length + 1);
                
        System.arraycopy(array, 0, newArray, 0, array.length);
        newArray[array.length] = element;
        return newArray;
    }
    
    /**
     * Concat two arrays. The type of the concatened array is one of the arrays
     * specified in arguments. If left array is assignable from right array, the
     * type is the one of the left array. If right array is assignable from left
     * array, the type is the one of the right array. Otherwise the type is 
     * Object[]
     * 
     * @param arrayLeft Left part of the array to concat
     * @param arrayRight Right part of the array to concat
     * @return Concatened array
     */
    public static Object[] concat(Object[] arrayLeft, Object[] arrayRight) {
            
        if (arrayLeft == null) {
            if (arrayRight != null) {
                return arrayRight;
            }
            return new Object[0];
        }
        
        if (arrayRight == null) {
            return arrayLeft;
        }
        
        Class componentType = null;
        if (arrayLeft.getClass().isAssignableFrom(arrayRight.getClass())) {
            componentType = arrayLeft.getClass().getComponentType();
            
        } else if (arrayRight.getClass().isAssignableFrom(arrayLeft.getClass())) {
            componentType = arrayRight.getClass().getComponentType();
            
        } else {
            componentType = Object.class;
        }
        
        Object[] newArray = 
            (Object[]) Array.newInstance(
                componentType,
                arrayLeft.length + arrayRight.length);

        System.arraycopy(arrayLeft, 0, newArray, 0, arrayLeft.length);
        System.arraycopy(arrayRight, 0, newArray, arrayLeft.length, arrayRight.length);
        return newArray;
    }
}