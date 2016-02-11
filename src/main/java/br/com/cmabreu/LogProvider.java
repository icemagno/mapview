package br.com.cmabreu;

import java.util.ArrayList;
import java.util.List;

public class LogProvider {
	private static LogProvider instance;
	private List<String> logList;
	
	public static LogProvider getInstance() {
		if ( instance == null ) {
			instance = new LogProvider(); 
		}
		return instance;
	}

	public synchronized void addLog( String log ) {
		logList.add( log );
	}
	
	public LogProvider() {
		logList = new ArrayList<String>();
	}
	
	public synchronized List<String> getLog() {
		List<String> tempLogList = new ArrayList<String>( logList );
		logList.clear();
		return tempLogList;
	}
	
}
