package com.baselib.watchdog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.baselib.bean.container.BeanHelper;
import com.baselib.bean.metadata.AnnotationHelper;
import com.baselib.bean.metadata.MetaTypeData;
import com.baselib.bean.metatype.MetaAnnotationProcessor;
import com.baselib.util.ClassUtil;

/**
 * 
 * 
 * @Auhtor: Mustafa 
 */

// this class can be parameterik using ProcessedMetaMetaType as parameter
public class WatchDogTypeProcessor extends MetaAnnotationProcessor {

	public void handle() {
		
		//meta to metameta
		Set<String> classSet = AnnotationHelper.getClassList(WatchDogType.class);
		
	    TreeMap<Integer,List<MetaTypeData<WatchDogType>>> moduleTypeMap = new TreeMap<Integer,List<MetaTypeData<WatchDogType>>>();
	    
	    List<MetaTypeData<WatchDogType>> moduleTypeList;
	    
	    if (classSet != null) {
	    	
	    for (String classString : classSet) {

	    	moduleTypeList = null;
//todo check implementing module interface..	    	

	    	Class<? extends WatchDog> moduleClass = ClassUtil.forName(classString);
	    	
	    	WatchDogType metaTypeData = moduleClass.getAnnotation(WatchDogType.class);
	    	
	    	// if there is direct relation..
	    	if (metaTypeData != null) {
	    		
	    		moduleTypeList = moduleTypeMap.get(metaTypeData.order());

	    		if (moduleTypeList == null) {
	    			moduleTypeList = new ArrayList<MetaTypeData<WatchDogType>>();
	    		}
	    				
	    		moduleTypeList.add(new MetaTypeData<WatchDogType>(metaTypeData,moduleClass));
	    	
	    		moduleTypeMap.put(metaTypeData.order(), moduleTypeList);
	    	}
	    }
	    
	      Iterator<Entry<Integer,List<MetaTypeData<WatchDogType>>>> iterAnnoType = moduleTypeMap.entrySet().iterator();

	      while(iterAnnoType.hasNext()) {
	    	  
	    	  Entry<Integer,List<MetaTypeData<WatchDogType>>> entry = iterAnnoType.next();
	    	  
	    	  for (Iterator<MetaTypeData<WatchDogType>> modulTypeDataIter = entry.getValue().iterator(); modulTypeDataIter.hasNext(); ) {

	    		  MetaTypeData<WatchDogType> metaTypeData = modulTypeDataIter.next();   

//TODO chec class type..

	    		  Class<? extends WatchDog> handlerClass = (Class<? extends WatchDog>) metaTypeData.getTypeClass();
	    		  
	    		  WatchDog watchDog = BeanHelper.getBean(handlerClass);

	    		  WatchDogManager watchDogManager = BeanHelper.getBean(WatchDogManager.class);
	    		  
	    		  watchDogManager.start(metaTypeData.getType(), watchDog);

	    	  }
	      }
	  }
   }

}
