package br.com.cmabreu.websockets;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WsOutbound;

public class WSMessageInbound extends MessageInbound {

	 @Override
	 protected void onBinaryMessage(ByteBuffer message)	 throws IOException {
		 throw new UnsupportedOperationException("Binary message not supported.");
	 }
	  
	 @Override
	 protected void onTextMessage(CharBuffer message) throws IOException {
		 String msg = message.toString();
		 msg = "(" + System.currentTimeMillis()+") "+ msg;
		 broadcast(msg);
	 }
	  
	 private void broadcast(String message) {
		 //write some code to process the message
	 } 	
	
	 @Override
	 protected void onOpen(WsOutbound outbound) {
		 
	 }
	  
	 @Override
	 protected void onClose(int status) {
		 
	 }
	 
}
