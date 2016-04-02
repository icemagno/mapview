package br.com.cmabreu.websockets;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.Session;


public class ClientList {
	private static ClientList instance;
	private Set<Session> userSessions = Collections.synchronizedSet( new HashSet<Session>() );
	
	public static ClientList getInstance() {
		if ( instance == null ) {
			instance = new ClientList();
		}
		return instance;
	}
	
	public void addClient( Session userSession ) {
		System.out.println("new client");
		userSessions.add(userSession);		
	}
	
	public void removeClient( Session userSession ) {
		System.out.println("lost client");
		userSessions.remove(userSession);
	}
	
	public void message( String message ) {
		message( message, null);
	}

	public void message( String message, Session userSession ) {
        for (Session session : userSessions) {
            session.getAsyncRemote().sendText( message );
			System.out.println( message );

        }		
	}

}
