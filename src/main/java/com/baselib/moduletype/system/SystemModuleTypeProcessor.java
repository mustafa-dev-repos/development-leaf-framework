package com.baselib.moduletype.system;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.baselib.bean.binder.BindingHelper;
import com.baselib.bean.container.BeanHelper;
import com.baselib.bean.metadata.AnnotationHelper;
import com.baselib.bean.metatype.MetaAnnotationProcessor;
import com.baselib.moduletype.Module;
import com.baselib.moduletype.interceptor.ModulePostBindInterceptor;
import com.baselib.util.ClassUtil;

/**
 * 
 * 
 * @Auhtor: Mustafa 
 */
// this class can be parameterik using ProcessedMetaMetaType as parameter
public class SystemModuleTypeProcessor extends MetaAnnotationProcessor  {

	public void handle() {
		
		//meta to metameta
		Set<String> classSet = AnnotationHelper.getClassList(SystemModuleType.class);
		
	    TreeMap<Integer,List<ModuleTypeData>> moduleTypeMap = new TreeMap<Integer,List<ModuleTypeData>>();
	    
	    List<ModuleTypeData> moduleTypeList;
	    
	    if (classSet != null) {
	    
	    for (String classString : classSet) {

	    	moduleTypeList = null;
//todo check implementing module interface..	    	

	    	Class<? extends Module> moduleClass = ClassUtil.forName(classString);
	    	
	    	SystemModuleType metaTypeData = moduleClass.getAnnotation(SystemModuleType.class);
	    	
	    	// if there is direct relation..
	    	if (metaTypeData != null) {
	    		
	    		moduleTypeList = moduleTypeMap.get(metaTypeData.order());

	    		if (moduleTypeList == null) {
	    			
	    			moduleTypeList = new ArrayList<ModuleTypeData>();
	    		}
	    				
	    		moduleTypeList.add(new ModuleTypeData(metaTypeData,moduleClass));
	    	
	    		moduleTypeMap.put(metaTypeData.order(), moduleTypeList);
	    	}
	    }
	    
	      Iterator<Entry<Integer,List<ModuleTypeData>>> iterAnnoType = moduleTypeMap.entrySet().iterator();

	      while(iterAnnoType.hasNext()) {
	    	  
	    	  Entry<Integer,List<ModuleTypeData>> entry = iterAnnoType.next();
	    	  
	    	  for (Iterator<ModuleTypeData> modulTypeDataIter = entry.getValue().iterator(); modulTypeDataIter.hasNext(); ) {

	    		  ModuleTypeData moduleTypeData = modulTypeDataIter.next();   

	    		  Class<? extends Module> handlerClass = moduleTypeData.getModuleClass();

	    		  Module moduleHandler = ClassUtil.newInstance(handlerClass);

	    		  moduleHandler.bindBeans(BindingHelper.getBinder());

	    		  ModulePostBindInterceptor interceptor = BeanHelper.getBean(ModulePostBindInterceptor.class);;
	    		  interceptor.handle(moduleHandler);
	    	  }
	      }
	   }
	}

	private class ModuleTypeData {

		private SystemModuleType moduleType;
		private Class<? extends Module> moduleClass;

		public ModuleTypeData(SystemModuleType moduleType,Class<? extends Module> moduleClass){
		
				this.moduleType = moduleType;
				this.moduleClass = moduleClass;
		}
		/**
		 * @return the moduleClass
		 */
		protected Class<? extends Module> getModuleClass() {
			return moduleClass;
		}
		/**
		 * @param moduleClass the moduleClass to set
		 */
		protected void setModuleClass(Class<? extends Module> moduleClass) {
			this.moduleClass = moduleClass;
		}
		/**
		 * @return the moduleType
		 */
		protected SystemModuleType getModuleType() {
			return moduleType;
		}
		/**
		 * @param moduleType the moduleType to set
		 */
		protected void setModuleType(SystemModuleType moduleType) {
			this.moduleType = moduleType;
		}
	}


}
