package com.baselib.watchdog;

import java.text.MessageFormat;

import com.baselib.log.LogHelper;

/**
 *
 * 
 * @Auhtor: Mustafa   
 */

public class DefaultWatchDogManager implements WatchDogManager {

   private static final String DEBUG_REFRESH = "Refreshing the [{0}]";

   private static final String DEBUG_INSTANTIATING = "Instantiating [{0}]";

   private static final String DEBUG_REFRESHED_SUCCESSFULLY = "[{0}] refreshed successfully";

   private static final String DEBUG_WATCHDOG_WILL_SLEEP = "[{0}] will sleep for [{1}] miliseconds";
   
   private static final String DEBUG_WATCHDOG_WILL_STOP = "[{0}] has been requested to stop";
   
   private static final String WARN_REFRESH_ALREADY_RUNNING = "[{0}] is already running.";

   private static final String WARN_EXCEPTION_OCCURED_WHILE_RUNNING = "Exception occured while running [{0}]";

   private static final String INFO_WATCHDOG_STARTED = "[{0}] has been started";

   private static final String INFO_REQUEST_FOR_STARTING = "Requesting [{0}] to start";

   private static final String INFO_WATCHDOG_IS_RUNNING = "[{0}] is running";
   
   private static final String INFO_WATCHDOG_WAS_STOPED = "[{0}] was stoped";
   
   private static final String CLASS_NAME = DefaultWatchDogManager.class.getName();

   private static LogHelper log = LogHelper.getLog();

   private Thread currentThread = null;

   private volatile boolean alive = false;

   private long refreshRate = 60 * 60 * 1000 ;

   private WatchDog watchDog;
	
   WatchDogType watchDogType;

   public DefaultWatchDogManager() {
	   
       super();
       
   }

   /**
    * Start the Thread
    */

   public void start(WatchDogType watchDogType, WatchDog watchDog ) {

	   	if (alive) {
    	  
	   		log.warn(MessageFormat.format(WARN_REFRESH_ALREADY_RUNNING , new Object[] { watchDogType.name()} ));
	   		return;
      	}

      synchronized (this) {
    	  
         if (alive) {

        	 log.warn(MessageFormat.format(WARN_REFRESH_ALREADY_RUNNING , new Object[] { watchDogType.name()} ));
             
             return;
         }
         
         log.debug( MessageFormat.format(DEBUG_INSTANTIATING , new Object[] { watchDogType.name() }) );
          
         log.info( MessageFormat.format(INFO_REQUEST_FOR_STARTING , new Object[] { watchDogType.name() }) );
          
         alive = true;
       
         this.watchDog = watchDog;
         
         this.watchDogType = watchDogType;

         this.refreshRate = watchDogType.refreshRateInMs();

         currentThread = new Thread(this);
         
         currentThread.setDaemon(true);

         currentThread.setContextClassLoader(watchDog.getClass().getClassLoader());

         // start the thread
         currentThread.start();

         log.info( MessageFormat.format(INFO_WATCHDOG_STARTED, new Object[] { watchDogType.name() }) );

      }
   }

   public void stop() {
	   
	   	 alive = false;

	   	 log.debug(MessageFormat.format(DEBUG_WATCHDOG_WILL_STOP , new Object[] { watchDogType.name() }));
   }

   public void setRefreshRate(long _refreshRate) {
      refreshRate = _refreshRate;
   }

   /**
    * @see java.lang.Runnable#run()
    */
   public void run() {
	   
	   		log.info( MessageFormat.format(INFO_WATCHDOG_IS_RUNNING , new Object[] { watchDogType.name() }));
	   		
      while (alive) {
    	  
         try {
        	 
        	log.debug( MessageFormat.format(DEBUG_WATCHDOG_WILL_SLEEP , new Object[] { watchDogType.name(), refreshRate} ) );
            
            Thread.sleep( refreshRate );

            log.debug(MessageFormat.format(DEBUG_REFRESH , new Object[] { watchDogType.name()} ));

            watchDog.refresh();

            log.debug( MessageFormat.format(DEBUG_REFRESHED_SUCCESSFULLY , new Object[] { watchDogType.name() } ) );

         } catch (Exception e) {
        	 
        	log.warn( MessageFormat.format(WARN_EXCEPTION_OCCURED_WHILE_RUNNING , new Object[] { watchDogType.name() }) );
            
        	log.error(CLASS_NAME, e.getMessage(), e);
         }
      }
      
      		log.info(MessageFormat.format(INFO_WATCHDOG_WAS_STOPED , new Object[] { watchDogType.name() }));

   }

   public boolean isAlive() {
	   
	   return alive;
   }
}
