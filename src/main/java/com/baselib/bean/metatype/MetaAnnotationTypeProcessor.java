package com.baselib.bean.metatype;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.baselib.bean.metadata.AnnotationHelper;
import com.baselib.util.ClassUtil;

/**
 * 
 * @Auhtor: Mustafa 
 */

public class MetaAnnotationTypeProcessor {
	
	private static Map<String,TreeMap<Integer,List<MetaAnnotationType>>> annotationTypeMap;

	static {
		
		annotationTypeMap = constructMetaTypeMap();
	}

	
	public static Map<String,TreeMap<Integer,List<MetaAnnotationType>>> constructMetaTypeMap() {
		
		Set<String> classSet = AnnotationHelper.getClassList(MetaAnnotationType.class);
		
		Map<String,TreeMap<Integer,List<MetaAnnotationType>>> annotationTypeMap = new HashMap<String,TreeMap<Integer,List<MetaAnnotationType>>>();

	    List<MetaAnnotationType> metaAnnoList;

	    for (String classString : classSet) {
	    
	    	Class<?> clazz = ClassUtil.forName(classString);
	    	
	    	MetaAnnotationType metaTypeData = clazz.getAnnotation(MetaAnnotationType.class);

	    	// if there is direct dependency..
	    	if (metaTypeData != null) {
	    		
	    		if (!annotationTypeMap.containsKey(clazz.getName())) {
	    			annotationTypeMap.put(clazz.getName(), new TreeMap<Integer,List<MetaAnnotationType>>());
	    		}
	    		
	    		if (!annotationTypeMap.get(clazz.getName()).containsKey(metaTypeData.order())) {
	    			 annotationTypeMap.get(clazz.getName()).put(metaTypeData.order(), new ArrayList<MetaAnnotationType>());	    			
	    		}
	    		
	    		metaAnnoList = annotationTypeMap.get(clazz.getName()).get(metaTypeData.order());
	    		metaAnnoList.add(metaTypeData);
	    		
	    	}
	   }
	    
	    return annotationTypeMap;
	}

	public static void handleMetaType(Class<? extends Annotation> annotationType) {

		  TreeMap<Integer,List<MetaAnnotationType>> annoMap = annotationTypeMap.get(annotationType.getName());

		  if (annoMap != null && annoMap.size() > 0 ) {  

		  Iterator<Entry<Integer,List<MetaAnnotationType>>> iterAnnoType = annoMap.entrySet().iterator();

	      while(iterAnnoType.hasNext()) {
	    	  
	    	  Entry<Integer,List<MetaAnnotationType>> entry = iterAnnoType.next();

	    	  for (Iterator<MetaAnnotationType> metaTypeDataIter = entry.getValue().iterator(); metaTypeDataIter.hasNext(); ) {

	    		  MetaAnnotationType metaAnnotationType = metaTypeDataIter.next();
	    		  
	    		  Class<? extends MetaAnnotationProcessor> handlerClass = metaAnnotationType.handler();

	    		  MetaAnnotationProcessor annoTypeHandler = ClassUtil.newInstance(handlerClass);
	    	  
	    		  annoTypeHandler.handle();
		      }
	       }
	      }
	}
}
