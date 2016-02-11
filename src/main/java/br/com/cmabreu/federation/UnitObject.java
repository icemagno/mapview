package br.com.cmabreu.federation;

import hla.rti1516e.ObjectInstanceHandle;
import br.com.cmabreu.units.BaseUnit;
import br.com.cmabreu.units.IUnit;

// All our objects in RTI must be represented by Java classes. 
public class UnitObject extends BaseUnit implements IUnit {
	// To hold the RTI object handle for a specific Tank
	private ObjectInstanceHandle instance;
	private Position position;
	
	public void setPosition(Position position) {
		setLatitude( position.getLatitude() );
		setLongitude( position.getLongitude() );
		this.position = position;
	}
	
	public Position getPosition() {
		return position;
	}
	
	// Constructor. Must hold the instance handle of this Tank in RTI.
	public UnitObject( ObjectInstanceHandle instance ) {
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

	
}
