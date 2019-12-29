package com.baselib.beantype;

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

public class BeanTypeProcessor extends MetaAnnotationProcessor {

	public void handle() {
		
		//meta to metameta
		Set<String> classSet = AnnotationHelper.getClassList(BeanFactoryType.class);
		
	    TreeMap<Integer,List<MetaTypeData<BeanFactoryType>>> configTypeMap = new TreeMap<Integer,List<MetaTypeData<BeanFactoryType>>>();
	    
	    List<MetaTypeData<BeanFactoryType>> configTypeList;
	    
	    if (classSet != null) {
	    for (String classString : classSet) {

	    	configTypeList = null;

	    	//todo check implementing module interface..	    	
	    	Class<? extends Config> configClass = ClassUtil.forName(classString);
	    	
	    	BeanFactoryType metaTypeData = configClass.getAnnotation(BeanFactoryType.class);
	    	
	    	// if there is direct relation..
	    	if (metaTypeData != null) {
	    		
	    		configTypeList = configTypeMap.get(metaTypeData.order());

	    		if (configTypeList == null) {
	    			configTypeList = new ArrayList<MetaTypeData<BeanFactoryType>>();
	    		}
	    				
	    		configTypeList.add(new MetaTypeData<BeanFactoryType>(metaTypeData, configClass));
	    			
	    		configTypeMap.put(metaTypeData.order(), configTypeList);
	    	}
	    }
	    
	      Iterator<Entry<Integer,List<MetaTypeData<BeanFactoryType>>>> iterAnnoType = configTypeMap.entrySet().iterator();

	      while(iterAnnoType.hasNext()) {
	    	  
	    	  Entry<Integer,List<MetaTypeData<BeanFactoryType>>> entry = iterAnnoType.next();
	    	  
	    	  for (Iterator<MetaTypeData<BeanFactoryType>> configTypeDataIter = entry.getValue().iterator(); configTypeDataIter.hasNext(); ) {

	    		  MetaTypeData configTypeData = configTypeDataIter.next();   

	    		  Class<? extends Config> handlerClass = configTypeData.getTypeClass();

	    		  Config configHandler = ClassUtil.newInstance(handlerClass);

	    		  configHandler.config(ConfigHelper.getConfigManager());
	    	  }
	      }
	  }	      
	}


}

