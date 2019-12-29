package com.leaf.framework.support.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 
 * @Auhtor: Mustafa 
 */

public class LeafContextListener implements ServletContextListener {

	public void contextDestroyed(ServletContextEvent arg0) {
		
	}

	public void contextInitialized(ServletContextEvent event) {
		
		 LeafWebInitializer.initialize(event.getServletContext());
	}

}
