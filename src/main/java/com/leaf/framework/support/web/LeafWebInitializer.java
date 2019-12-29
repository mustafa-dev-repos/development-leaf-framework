package com.leaf.framework.support.web;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;

import org.scannotation.WarUrlFinder;

import com.baselib.startup.StartupHelper;

/**
 * 
 * 
 * @Auhtor: Mustafa 
 */

public class LeafWebInitializer {

	// scan jar parameter
	private static final String SCAN_JARS = "com.leaf.framework.SCAN_JARS";

	//TODO refactor for version numbering like xx1_3.jar
	// default jars for scanning
	private static final String DEFAULT_SCAN_JARS = ".";

	// jar file token
	private static final String TOKEN = ",";
	
	private LeafWebInitializer() {
		
	}

	public static void initialize(ServletContext context) {
		
        String scanJars = context.getInitParameter(SCAN_JARS);
        
        //TODO remove all blanks from scanJars.. 
        if (scanJars != null && !"".equals(scanJars) ) {
        	
        	scanJars = scanJars.trim() + "," + DEFAULT_SCAN_JARS;
        }
        else {
        	
        	scanJars = DEFAULT_SCAN_JARS;
        }
        
        Set<String> jarSet = getJars(scanJars);
        
		List<URL> urls = new ArrayList<URL>();

		urls.add(WarUrlFinder.findWebInfClassesPath(context));

		URL[] urlLibs = WarUrlFinder.findWebInfLibClasspaths(context);

		if (urlLibs != null && urlLibs.length > 0) {

			for (int i = 0; i < urlLibs.length; i++) {
				
				String jarName = getURLFileName(urlLibs[i].getFile());

                if (jarName != null && jarName.endsWith(".jar") && jarSet.contains(jarName)) {

                	urls.add(urlLibs[i]);
				}
			}
		}
        	
		if (urls != null && urls.size() > 0) {

			URL[] urlArr = new URL[urls.size()];

			urls.toArray(urlArr);
			
			StartupHelper.start(urlArr);
		}
		else {
			
			StartupHelper.start();
		}

	}

	private static Set<String> getJars(String jarStr) {
		
		Set<String> jarSet = new HashSet<String>(); 
		
		StringTokenizer tokenizer = new StringTokenizer(jarStr.trim(), TOKEN );

		while (tokenizer.hasMoreElements()) {
			
			 jarSet.add(((String) tokenizer.nextElement()).toLowerCase());
		}

		return jarSet;
	
	}
	
	private static String getURLFileName( String fileName ) {

        String jarName = null;
        
        int index1 = fileName.lastIndexOf("/");
        int index2 = fileName.lastIndexOf("\\");

        if ( index1 > index2 ) {
            jarName = fileName.substring(index1 + 1, fileName.length() );

        } else {
            jarName = fileName.substring(index2 + 1, fileName.length() );
        }

        return jarName.toLowerCase();
    }
}
