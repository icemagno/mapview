package br.com.cmabreu;

import hla.rti1516e.ObjectInstanceHandle;

import java.util.Random;

// All our objects in RTI must be represented by Java classes. 
public class AircraftObject {
	// To hold the RTI object handle for a specific Tank
	private ObjectInstanceHandle instance;
	// All attributes from the SOM must be here
	private int maxRange;
	private Position position;
	private String name;
	private String serial;
	private String imageName = "bomber-2";
	private int unitType = UNKNOWN;
	
	public static final int ME = 100;
	public static final int FRIEND = 101;
	public static final int FOE = 102;
	public static final int UNKNOWN = 103;
	
	// Update the Tank state... just walk a little by Longitude axis.
	public void update() {
		Random random = new Random();
		double rate = random.nextInt(20)+1;
		position.setLongitude( position.getLongitude() + ( rate / 10000 )  );
		position.setLatitude( position.getLatitude() + ( rate / 10000 )  );
	}
	
	public String getImageName() {
		return imageName;
	}
	
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	
	// Constructor. Must hold the instance handle of this Tank in RTI.
	public AircraftObject( ObjectInstanceHandle instance, int unitType ) {
		this.maxRange = 100;
		this.instance = instance;
		this.unitType = unitType;

	}
	
	// Check if a given object is this specific Tank
	public boolean isMe( ObjectInstanceHandle obj ) {
		return obj.equals( instance );
	}
	
	// Just a getter...
	public ObjectInstanceHandle getHandle() {
		return instance;
	}

	public int getMaxRange() {
		return maxRange;
	}
	
	public void setMaxRange(int maxRange) {
		this.maxRange = maxRange;
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
	
	public int getUnitType() {
		return unitType;
	}
	
	public void setUnitType(int unitType) {
		this.unitType = unitType;
	}
	
	
	
}
