package com.baselib.phase;

import java.text.MessageFormat;

import com.baselib.exception.TechnicalException;

/**
 * 
 * 
 * @Auhtor: Mustafa 
 */

public class PhaseHelper {

	private static final String CLASS_NAME = PhaseHelper.class.getName();
	
	private static final Phase phases[] = { Phase.WAITING_FOR_START,
											Phase.META_DATA_SCAN,
											Phase.BEAN_FACTORY_CONSTRUCTION, 
							   				Phase.SYSTEM_CONFIG_PROCESSING, 
							   				Phase.SYSTEM_MODULE_PROCESSING,
							   				Phase.APPLICATION_CONFIG_PROCESSING, 
							   				Phase.APPLICATION_MODULE_PROCESSING,
							   				Phase.RELEASE_WATCHDOGS, 
							   				Phase.READY_FOR_USE
										  };

	private static final String ERROR_LEVEL_SET_NOT_ALLOWED = "Setting system level to [{0}] phase from [{1}] phase is not allowed. To switch to [{2}] phase system must be in [{3}] phase.";
	
	private static Phase level = Phase.WAITING_FOR_START;
	
	public static Phase getSystemLevel() {
		
		return level;
	}
	
//TODO handle restart case , synch method..
	public static void startPhases(PhaseContext context) {
//TODO put synch hereee..
		
		for (int i = 0 ; i< phases.length ; i++ ) {
			
			 setLevel(phases[i]);
			 phases[i].phaseHandler(context);
		}
		
	}
	
	private static void setLevel(Phase newLevel) {

//handle update case..		
		if (level == Phase.READY_FOR_USE)
			return;
		
		if (newLevel.getPreviousLevel().geValue() != level.geValue()) {
			
			String message = MessageFormat.format(ERROR_LEVEL_SET_NOT_ALLOWED, new Object[] { newLevel.name().toString(), level.name().toString(), newLevel.name().toString(), newLevel.getPreviousLevel().name().toString() });
			
			throw new TechnicalException(CLASS_NAME, message);
		}
		
		level = newLevel;
	}
	
}
