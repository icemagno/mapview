package br.com.cmabreu;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.cmabreu.federation.MapViewFederate;


@WebListener
public class Startup implements ServletContextListener {
	private Logger logger = LogManager.getLogger( this.getClass().getName() );
	private ScheduledExecutorService scheduler;
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		logger.debug("init system");
		MapViewFederate.getInstance().startFederate();

		
		UnitListProvider ulp = new UnitListProvider();
		scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(ulp, 0, 100 , TimeUnit.MILLISECONDS);		
		
        
		logger.debug("done.");
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		logger.debug("Server shutdown detected! Stopping Federation...");
		try {
			MapViewFederate.getInstance().exitFromFederation();
		} catch ( Exception e ) {
			
		}
		logger.debug("done.");
	}
}
