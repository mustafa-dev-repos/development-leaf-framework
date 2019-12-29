package com.baselib.config.system;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.baselib.bean.metadata.AnnotationHelper;
import com.baselib.bean.metadata.MetaTypeData;
import com.baselib.bean.metatype.MetaAnnotationProcessor;
import com.baselib.config.Config;
import com.baselib.config.ConfigHelper;
import com.baselib.util.ClassUtil;

/**
 * 
 * 
 * @Auhtor: Mustafa 
 */

public class SystemConfigTypeProcessor extends MetaAnnotationProcessor {

	public void handle() {
		
		//meta to metameta
		Set<String> classSet = AnnotationHelper.getClassList(SystemConfigType.class);
		
	    TreeMap<Integer,List<MetaTypeData<SystemConfigType>>> configTypeMap = new TreeMap<Integer,List<MetaTypeData<SystemConfigType>>>();
	    
	    List<MetaTypeData<SystemConfigType>> configTypeList;

	    if (classSet != null) {
	    	
	    for (String classString : classSet) {

	    	configTypeList = null;

	    	//todo check implementing module interface..	    	
	    	Class<? extends Config> configClass = ClassUtil.forName(classString);
	    	
	    	SystemConfigType metaTypeData = configClass.getAnnotation(SystemConfigType.class);
	    	
	    	// if there is direct relation..
	    	if (metaTypeData != null) {
	    		
	    		configTypeList = configTypeMap.get(metaTypeData.order());

	    		if (configTypeList == null) {
	    			
	    			configTypeList = new ArrayList<MetaTypeData<SystemConfigType>>();
	    		}
	    				
	    		configTypeList.add(new MetaTypeData<SystemConfigType>(metaTypeData, configClass));
	    			
	    		configTypeMap.put(metaTypeData.order(), configTypeList);
	    	}
	    }
	    
	      Iterator<Entry<Integer,List<MetaTypeData<SystemConfigType>>>> iterAnnoType = configTypeMap.entrySet().iterator();

	      while(iterAnnoType.hasNext()) {
	    	  
	    	  Entry<Integer,List<MetaTypeData<SystemConfigType>>> entry = iterAnnoType.next();
	    	  
	    	  for (Iterator<MetaTypeData<SystemConfigType>> configTypeDataIter = entry.getValue().iterator(); configTypeDataIter.hasNext(); ) {

	    		  MetaTypeData configTypeData = configTypeDataIter.next();   

	    		  Class<? extends Config> handlerClass = configTypeData.getTypeClass();

	    		  Config configHandler = ClassUtil.newInstance(handlerClass);

	    		  configHandler.config(ConfigHelper.getConfigManager());
	    	  }
	      }
	}
    }


}

