package com.baselib.startup;

import java.net.URL;

import com.baselib.phase.PhaseContext;
import com.baselib.phase.PhaseHelper;

public class StartupHelper {
	
	public static void start() {
		
		start(null);
	}

	public static void start(URL[] urls) {

		 PhaseContext context = new PhaseContext(urls); 

		 //TODO add log messages here..

		 PhaseHelper.startPhases(context);
		
	}
}
