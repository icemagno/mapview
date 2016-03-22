package br.com.cmabreu.websockets;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;

/*
	*** STRUTS.XML ***
	<constant name="struts.action.excludePattern" value="/echo"/>


  	*** WEB.XML ***
	<servlet>
		<servlet-name>WsChatServlet</servlet-name>
		<servlet-class>br.com.cmabreu.websockets.Teste</servlet-class>
	</servlet>
	<servlet-mapping>
		<servlet-name>WsChatServlet</servlet-name>
		<url-pattern>/echo</url-pattern>
	</servlet-mapping>  
 */

public class Teste extends WebSocketServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected StreamInbound createWebSocketInbound(String protocol,	HttpServletRequest request) {
		StreamInbound si = new WSMessageInbound();
		ClientList.getInstance().addClient( si );
		return si;
	}

}
