package com.baselib.phase;

import com.baselib.bean.metadata.AnnotationHelper;
import com.baselib.bean.metatype.MetaAnnotationTypeProcessor;
import com.baselib.beantype.BeanFactoryType;
import com.baselib.config.ConfigType;
import com.baselib.config.system.SystemConfigType;
import com.baselib.moduletype.ModuleType;
import com.baselib.moduletype.system.SystemModuleType;
import com.baselib.watchdog.WatchDogType;

/**
 * 
 * 
 * @Auhtor: Mustafa 
 */

public enum Phase {

	//initial point 
	WAITING_FOR_START {
		
				  public int geValue() { return 1; } 
				  public Phase getPreviousLevel(){ return WAITING_FOR_START;}
			   	  public void phaseHandler(PhaseContext context){ //TODO log here..
			   		  
			   	  }
			}, 

	META_DATA_SCAN { 
				
				  public int geValue() { return 2; } 
				  public Phase getPreviousLevel(){ return WAITING_FOR_START;}
			   	  public void phaseHandler(PhaseContext context){ AnnotationHelper.scan(context); }
			   	  
		    }, 
 
	BEAN_FACTORY_CONSTRUCTION {
		    	
				  public int geValue() { return 3; } 
				  public Phase getPreviousLevel(){ return META_DATA_SCAN;}
				  public void phaseHandler(PhaseContext context){ MetaAnnotationTypeProcessor.handleMetaType(BeanFactoryType.class); }
										 
	}, 
			   
	//config type operations started up 
	SYSTEM_CONFIG_PROCESSING {
			   		   
			   	  public int geValue() { return 4; } 
			   	  public Phase getPreviousLevel(){ return BEAN_FACTORY_CONSTRUCTION;}
			   	  public void phaseHandler(PhaseContext context){MetaAnnotationTypeProcessor.handleMetaType(SystemConfigType.class); }
			   	  
	}, 

	//module type operations started up	
	SYSTEM_MODULE_PROCESSING {
			   		   
			   	  public int geValue() { return 5; } 
			   	  public Phase getPreviousLevel(){ return SYSTEM_CONFIG_PROCESSING;}
   		   		  public void phaseHandler(PhaseContext context){ MetaAnnotationTypeProcessor.handleMetaType(SystemModuleType.class); }
					 
	}, 
	
	APPLICATION_CONFIG_PROCESSING {
				
				  public int geValue() { return 6; } 
				  public Phase getPreviousLevel(){ return SYSTEM_MODULE_PROCESSING;}
				  public void phaseHandler(PhaseContext context){ MetaAnnotationTypeProcessor.handleMetaType(ConfigType.class); }
						 
	}, 

	//module type operations started up	
	APPLICATION_MODULE_PROCESSING {
				
				  public int geValue() { return 7; } 
				  public Phase getPreviousLevel(){ return APPLICATION_CONFIG_PROCESSING; }
	   		   	  public void phaseHandler(PhaseContext context){ MetaAnnotationTypeProcessor.handleMetaType(ModuleType.class); }
	}, 
		
	RELEASE_WATCHDOGS {
				
				  public int geValue() { return 8; } 
				  public Phase getPreviousLevel(){ return APPLICATION_MODULE_PROCESSING; }
		   		  public void phaseHandler(PhaseContext context){ MetaAnnotationTypeProcessor.handleMetaType(WatchDogType.class); }
	}, 

	//system is ready for usage	
	READY_FOR_USE {
				
			   	  public int geValue() { return 9; } 
			   	  public Phase getPreviousLevel(){ return RELEASE_WATCHDOGS;}
			   	  public void phaseHandler(PhaseContext context){ }
	};

	public abstract int geValue();

	public abstract Phase getPreviousLevel();

	public abstract void phaseHandler(PhaseContext context);
	
}
