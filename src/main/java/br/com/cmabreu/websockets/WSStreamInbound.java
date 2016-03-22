package br.com.cmabreu.websockets;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WsOutbound;

public class WSStreamInbound extends StreamInbound {

	@Override
	protected void onBinaryData(InputStream is) throws IOException {
		WsOutbound outbound = getWsOutbound();
		
		int data = is.read();
		while (data != -1) {
			outbound.writeBinaryData(data);
			data = is.read();
		}
		outbound.flush();
	}

	@Override
	protected void onTextData(Reader reader) throws IOException	{
		// Obtain the outbound side of this WebSocket connection used for writing data to the client
		WsOutbound outbound = getWsOutbound();
		int character = reader.read();
		while (character != -1)	{
			outbound.writeTextData((char)character);
			character = reader.read();
		}
		outbound.flush();
	}
	
	 @Override
	 protected void onOpen(WsOutbound outbound) {
		 
	 }
	  
	 @Override
	 protected void onClose(int status) {
		 
	 }
	
	
}
