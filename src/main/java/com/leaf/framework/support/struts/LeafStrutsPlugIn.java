package com.leaf.framework.support.struts;

import javax.servlet.ServletException;

import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;

import com.leaf.framework.support.web.LeafWebInitializer;

/**
 * 
 * 
 * @Auhtor: Mustafa 
 */

public class LeafStrutsPlugIn implements PlugIn{

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public void init(ActionServlet arg0, ModuleConfig arg1) throws ServletException {

	    LeafWebInitializer.initialize(arg0.getServletContext());
		
	}

}


