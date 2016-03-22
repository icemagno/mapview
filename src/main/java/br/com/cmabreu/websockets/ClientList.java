package br.com.cmabreu.websockets;

import java.util.ArrayList;
import java.util.List;

import org.apache.catalina.websocket.StreamInbound;

public class ClientList {
	private static ClientList instance;
	private List<StreamInbound> clients;
	
	public static ClientList getInstance() {
		if ( instance == null ) {
			instance = new ClientList();
		}
		return instance;
	}
	
	public void addClient( StreamInbound client ) {
		List<StreamInbound> tempList = new ArrayList<StreamInbound>();
		for ( StreamInbound c : clients ) {
			// check to close and remove.
		}
		clients.add( client );
	}
	
	private ClientList() {
		clients = new ArrayList<StreamInbound>();
	}

}
