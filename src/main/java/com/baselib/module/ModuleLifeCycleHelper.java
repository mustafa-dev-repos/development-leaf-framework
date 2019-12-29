package com.baselib.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.baselib.bean.Bean;
import com.baselib.bean.container.BeanHelper;
import com.baselib.bean.lifecycle.Initialize;
import com.baselib.bean.lifecycle.Refresh;
import com.baselib.bean.metadata.AnnotationHelper;
import com.baselib.bean.metadata.BeanMetaData;
import com.baselib.bean.metadata.BeanMetaDataHelper;
import com.baselib.bean.metadata.MetaTypeData;
import com.baselib.moduletype.Module;
import com.baselib.moduletype.ModuleType;
import com.baselib.util.ClassUtil;

//Note: for the lifecycle support lifecycle annotations must be placed in interface definition and
//      interface class must be defined as singleton..

public class ModuleLifeCycleHelper {
	
	private static Map<String,TreeMap<Integer,List<MetaTypeData<Bean>>>> moduleBeanMap;
//TODO refactore here.. 	
	static {
		
		moduleBeanMap = getModuleBeanMap();
	}

	public static void initializeBeans(Module module) {
		
		TreeMap<Integer,List<MetaTypeData<Bean>>> beanMap = moduleBeanMap.get(module.getClass().getName());
		
	    if (beanMap != null && beanMap.size() > 0) {
	    
	    	Iterator<Entry<Integer,List<MetaTypeData<Bean>>>> iterBean = beanMap.entrySet().iterator();

	    	while(iterBean.hasNext()) {
	    	  
	    		Entry<Integer,List<MetaTypeData<Bean>>> entry = iterBean.next();

	    		for (Iterator<MetaTypeData<Bean>> metaTypeDataIter = entry.getValue().iterator(); metaTypeDataIter.hasNext(); ) {
	    
 	    			 MetaTypeData<Bean> metaAnnotationType = metaTypeDataIter.next();
	    		  
 	    			 Class<?> beanClass = metaAnnotationType.getTypeClass();

 	    			 BeanMetaData classDec = BeanMetaDataHelper.getClassDescriptor( beanClass );
	    	  
 	    			 // initialize only singleton objects, non singleton objects will be initialized at the creation time..
 	    			 if (classDec.isSingleton() && classDec.isLoadOnStart()) {
 	    				
 	    				 BeanHelper.getBean( beanClass );
 	    			 }
	    		}
	    	}
	    }
	}

	private static Map<String,TreeMap<Integer,List<MetaTypeData<Bean>>>> getModuleBeanMap() {
		
		Map<String,TreeMap<Integer,List<MetaTypeData<Bean>>>> moduleBeanMap = new HashMap<String,TreeMap<Integer,List<MetaTypeData<Bean>>>>();

		Set<String> initializeEnableBeanSet = AnnotationHelper.getClassList(Initialize.class);
		
		if (initializeEnableBeanSet != null) {
			
			Class<? extends Module> defaultModuleClass = DefaultModule.class;
			int orphanBeanDefaultOrder = DefaultModule.class.getAnnotation(ModuleType.class).order();
			Bean orphanBeanDefaultMetaData = DefaultBean.class.getAnnotation(Bean.class);

			Class<? extends Module> beanModuleClass = null;
			Bean beanMetaData = null;
			int order = 0;

			TreeMap<Integer,List<MetaTypeData<Bean>>> beanMap = null;
			List<MetaTypeData<Bean>>  beanMetaDataList = null;
			MetaTypeData<Bean> data = null;

			for (String classString : initializeEnableBeanSet) {
				
				beanMap = null;
				beanMetaDataList = null;
				data = null;
				
				Class<?> interfaceClass = ClassUtil.forName(classString);
				
				BeanMetaData metaData = BeanMetaDataHelper.getClassDescriptor(interfaceClass); 

				//provide life cycle only for interfaces.
				if (metaData.isInterface()) {
						
				    if (beanMetaData != null ) {

				    	beanMetaData = interfaceClass.getAnnotation( Bean.class );
				    	beanModuleClass = beanMetaData.module();
				    	order = beanMetaData.order();
				    }
				    else {
				    	
				    	beanMetaData = orphanBeanDefaultMetaData;
				    	beanModuleClass = defaultModuleClass;
				    	order = orphanBeanDefaultOrder;
				    }

				    if (!moduleBeanMap.containsKey(beanModuleClass.getName())) {
						
				    	moduleBeanMap.put(beanModuleClass.getName(), new TreeMap<Integer,List<MetaTypeData<Bean>>>()); 
				    }
				    
				    	beanMap = moduleBeanMap.get(beanModuleClass.getName());
				    	
					if (!beanMap.containsKey(order)) {
						
					    beanMetaDataList = new ArrayList<MetaTypeData<Bean>>();
					    beanMap.put(order, beanMetaDataList);
					}
					else {
						
						beanMetaDataList = beanMap.get(order);
					}
				    	
				    data = new MetaTypeData<Bean>(beanMetaData, interfaceClass);
	    		
				    beanMetaDataList.add(data);
				}	
				else {
				//TODO inform lifecycle support only available for interface classes..	
					
				}
			}
		}

		return moduleBeanMap;
	}
}
