package br.com.cmabreu;

import java.util.Random;
import java.util.UUID;

import hla.rti1516e.ObjectInstanceHandle;

// All our objects in RTI must be represented by Java classes. 
public class TankObject {
	// To hold the RTI object handle for a specific Tank
	private ObjectInstanceHandle instance;
	// All attributes from the SOM must be here
	private String model;
	private Position position;
	private String name;
	private String serial;
	
	// Update the Tank state... just walk a little by Longitude axis.
	public void update() {
		Random random = new Random();
		double rate = random.nextInt(9)+1;
		position.setLongitude( position.getLongitude() + ( rate / 10000 )  );
	}
	
	
	// Constructor. Must hold the instance handle of this Tank in RTI.
	public TankObject( ObjectInstanceHandle instance ) {
		this.model = UUID.randomUUID().toString().substring(1,5).toUpperCase();
		this.instance = instance;
	}
	
	// Check if a given object is this specific Tank
	public boolean isMe( ObjectInstanceHandle obj ) {
		return obj.equals( instance );
	}
	
	// Just a getter...
	public ObjectInstanceHandle getHandle() {
		return instance;
	}

	// Just a getter...
	public String getModel() {
		return model;
	}
	
	// Just a setter...	
	public void setModel(String model) {
		this.model = model;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}
	
	
	
}
