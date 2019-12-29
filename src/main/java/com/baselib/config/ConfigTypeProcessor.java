package com.baselib.config;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.baselib.bean.metadata.AnnotationHelper;
import com.baselib.bean.metadata.MetaTypeData;
import com.baselib.bean.metatype.MetaAnnotationProcessor;
import com.baselib.util.ClassUtil;

/**
 * 
 * 
 * @Auhtor: Mustafa 
 */

public class ConfigTypeProcessor extends MetaAnnotationProcessor {

	public void handle() {
		
		//meta to metameta
		Set<String> classSet = AnnotationHelper.getClassList(ConfigType.class);
		
	    TreeMap<Integer,List<MetaTypeData<ConfigType>>> configTypeMap = new TreeMap<Integer,List<MetaTypeData<ConfigType>>>();
	    
	    List<MetaTypeData<ConfigType>> configTypeList;
	    
	    if (classSet != null) {
	    for (String classString : classSet) {

	    	configTypeList = null;

	    	//todo check implementing module interface..	    	
	    	Class<? extends Config> configClass = ClassUtil.forName(classString);
	    	
	    	ConfigType metaTypeData = configClass.getAnnotation(ConfigType.class);
	    	
	    	// if there is direct relation..
	    	if (metaTypeData != null) {
	    		
	    		configTypeList = configTypeMap.get(metaTypeData.order());

	    		if (configTypeList == null) {
	    			configTypeList = new ArrayList<MetaTypeData<ConfigType>>();
	    		}
	    				
	    		configTypeList.add(new MetaTypeData<ConfigType>(metaTypeData, configClass));
	    			
	    		configTypeMap.put(metaTypeData.order(), configTypeList);
	    	}
	    }
	    
	      Iterator<Entry<Integer,List<MetaTypeData<ConfigType>>>> iterAnnoType = configTypeMap.entrySet().iterator();

	      while(iterAnnoType.hasNext()) {
	    	  
	    	  Entry<Integer,List<MetaTypeData<ConfigType>>> entry = iterAnnoType.next();
	    	  
	    	  for (Iterator<MetaTypeData<ConfigType>> configTypeDataIter = entry.getValue().iterator(); configTypeDataIter.hasNext(); ) {

	    		  MetaTypeData configTypeData = configTypeDataIter.next();   

	    		  Class<? extends Config> handlerClass = configTypeData.getTypeClass();

	    		  Config configHandler = ClassUtil.newInstance(handlerClass);

	    		  configHandler.config(ConfigHelper.getConfigManager());
	    	  }
	      }
	  }	      
	}


}

