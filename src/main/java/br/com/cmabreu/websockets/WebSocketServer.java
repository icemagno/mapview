package br.com.cmabreu.websockets;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;


/*
 *** STRUTS.XML ***
	<constant name="struts.action.excludePattern" value="/websocket"/>
*/


@ServerEndpoint(value="/websocket")
public class WebSocketServer  {

	@OnOpen
	public void onOpen(Session userSession) {
		ClientList.getInstance().addClient(userSession);
	}

    @OnClose
    public void onClose(Session userSession) {
    	ClientList.getInstance().removeClient(userSession);
    }
    
    @OnMessage
    public void onMessage(String message, Session userSession) {
    	System.out.println("Client talk: " + message );
    	// Clients just listen, don't talk!
    	// ClientList.getInstance().message( message, userSession );
    }
    
}
