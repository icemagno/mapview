package br.com.cmabreu;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.cmabreu.federation.MapViewFederate;


@WebListener
public class Startup implements ServletContextListener {
	private Logger logger = LogManager.getLogger( this.getClass().getName() );

	@Override
	public void contextInitialized(ServletContextEvent event) {
		logger.debug("init system");
		MapViewFederate.getInstance().startFederate();
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
