package br.com.cmabreu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class KeyReader implements Runnable {
	private IKeyReaderObserver observer;
    private BufferedReader in ;
    private boolean quit = false;
    private String quitKey = "q";

    public KeyReader( IKeyReaderObserver observer, String quitKey ) {
    	this.observer = observer;
		this.quitKey = quitKey;
	}
	
	public void readKeyUntilQuit() {
        in = new BufferedReader(new InputStreamReader( System.in ) );
        Thread t1=new Thread( this );
        t1.start();
         
        while(true) {
            try{
            	Thread.sleep(1);       
            }catch(InterruptedException e){
            	//
            }        	
            if( quit == true) break;
            observer.whenIdle();
        }
	}

	@Override
	public void run() {
        String msg = null;
        while(true) {
            try {
            	msg = in.readLine();
            } catch( IOException e ){
                e.printStackTrace();
            }
            if( msg.equals( quitKey ) ) {
            	quit = true;
            	break;
            } else {
            	observer.notify( msg );
            }
        }
	}
	
	
}
