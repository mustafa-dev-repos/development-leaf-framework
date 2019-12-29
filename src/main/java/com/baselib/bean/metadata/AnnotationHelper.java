package com.baselib.bean.metadata;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Set;

import org.scannotation.AnnotationDB;
import org.scannotation.ClasspathUrlFinder;
import org.scannotation.AnnotationDB.CrossReferenceException;

import com.baselib.log.LogHelper;
import com.baselib.phase.PhaseContext;
import com.baselib.timer.Timer;

/**
 * 
 * 
 * @Auhtor: Mustafa 
 */

public class AnnotationHelper  {

	private static AnnotationDB annotationMetaData ;

	private static String[] DEFAULT_IGNORED_PACKAGES = new String[] {"java", "javax", "org", "com.sun", "sun", "javassist","com.ibm"};
	
	private static final String INFO_ELAPSED_TIME_FOR_SCAN = "Elapsed time for annotation scanning is [{0}] miliseconds";
	
	private static final String ERROR_SCAN_FAILED = "Exception occured during annotation scan. Exception : [{0}]";

	private static LogHelper log = LogHelper.getLog();
	
	public AnnotationHelper() {
		
		super();
	}
	
	public static void scan(PhaseContext context) {
		
		try {
			
			Timer tt = new Timer();

			tt.startTimer();
			
			URL[] urls = context.getUrlsToScan();
			
			if (urls == null) {
		  
				urls = ClasspathUrlFinder.findClassPaths();  // scan java.class.path
			}
            
			annotationMetaData = new AnnotationDB();

			annotationMetaData.setIgnoredPackages(DEFAULT_IGNORED_PACKAGES);

			annotationMetaData.scanArchives(urls);
			annotationMetaData.crossReferenceMetaAnnotations();

			//annotationMetaData.crossReferenceImplementedInterfaces();

			tt.stopTimer();

            String message = MessageFormat.format(INFO_ELAPSED_TIME_FOR_SCAN, new Object[] {tt.getElapsedTime()});

			log.info(message);
					
			} catch (IOException e) {
				
	            String message = MessageFormat.format(ERROR_SCAN_FAILED, new Object[] { e.getMessage() } );

				log.error(message);
	
				e.printStackTrace();
				
			} catch (CrossReferenceException e) {
				e.printStackTrace();
			}
			
	}
	
	public static Set<String> getClassList(String anno) 
	{

		return annotationMetaData.getAnnotationIndex().get(anno);
	}

	public static boolean isUsingAnnotation(Class<?> clazz, Class<? extends Annotation>... annotation) 
	{

		Set<String> usingClassSet = null;
		
		for (int i = 0 ; i < annotation.length ;i++ ) {
		
			usingClassSet = getClassList( annotation[i] );
		
			if (usingClassSet != null && usingClassSet.contains( clazz.getName() )) {
				
				return true;
			}
		}
		
			return false;
	}

	public static Set<String> getClassList(Class<? extends Annotation> anno) 
	{
		return annotationMetaData.getAnnotationIndex().get(anno.getName());
	}
	
	public static Set<String> getMetaDataList(Class<?> clazz) 
	{
		return annotationMetaData.getClassIndex().get(clazz.getName());
	}
	
	public static Set<String> getMetaDataList(String clazzName) 
	{
		return annotationMetaData.getClassIndex().get(clazzName);
	}

}
