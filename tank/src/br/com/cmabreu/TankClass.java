package br.com.cmabreu;

import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleSet;
import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.ObjectClassHandle;
import hla.rti1516e.ObjectInstanceHandle;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.encoding.HLAinteger32BE;
import hla.rti1516e.encoding.HLAunicodeString;
import hla.rti1516e.exceptions.RTIexception;

import java.util.ArrayList;
import java.util.List;

public class TankClass {
	// The RTI Ambassador - So we can talk to the RTI from here.
	private RTIambassador rtiamb;
	// We must hold the handle of this class 
	private ObjectClassHandle tankHandle;
	// A list of Tank objects we will instantiate
	private List<TankObject> instances;
	// An encoder helper
	private EncoderDecoder encoder;
	// We must hold the handle for all attributes of this class
	// Just Model for now.
	private AttributeHandle modelHandle;
	private AttributeHandle nameHandle;
	private AttributeHandle serialHandle;
	private AttributeHandle imageNameHandle;
	private AttributeHandle positionHandle;
	private AttributeHandle unitTypeHandle;
	
	// Hold all our registered attributes  
	private AttributeHandleSet attributes;

	private void log( String message ) 	{
		System.out.println( "> " + message );
	}
	
	// Create a new Tank and register. Store it in our list of objects. 
	// Only the object owner can registerObjectInstance() of an object.
	// This method is used only by the Tank Federate.
	public ObjectInstanceHandle createNew(String name, String serial, Position position,int unitType) throws RTIexception {
		ObjectInstanceHandle coreObjectHandle = rtiamb.registerObjectInstance( tankHandle );
		TankObject to = new TankObject(coreObjectHandle, unitType);
		to.setName(name);
		to.setSerial(serial);
		to.setPosition(position);		
		log("Tank " + to.getName() + " created.");
		instances.add( to );
		return coreObjectHandle;
	}

	// Use this method if you are using this Java class ( TankClass.java ) by other
	// Federate ( Ex. a Radar ) . So when the RTI tells to your Radar Federate that 
	// there is a Tank nearby, it will give you the Tank's object handle then you can
	// store all tanks your Radar found. The two createNew() methods are exclusives:
	// This is used by Federates that not own the Tank object.
	public ObjectInstanceHandle createNew( ObjectInstanceHandle coreObjectHandle ) throws RTIexception {
		instances.add( new TankObject(coreObjectHandle, TankObject.UNKNOWN ) );
		return coreObjectHandle;
	}
	
	// When the Tank Federate sends the updates of its attributes, we must search
	// in our list of discovered Tanks to see if this specific Tank is in there.
	// If so, update its attributes. Do this only if you are NOT the Tank owner ( since you
	// haven't subscribed to your own attribute changes! )
	public TankObject update( AttributeHandleValueMap theAttributes, ObjectInstanceHandle theObject ) {
		// Find the Tank instance
		for ( TankObject tank : instances ) {
			if( tank.getHandle().equals( theObject) ) {
				// Update its attributes.
				for( AttributeHandle attributeHandle : theAttributes.keySet() )	{
					// Is the attribute the Tank's Model?
					if( attributeHandle.equals( modelHandle) ) {
						// Decode the RTI String type ( HLAunicodeString ) to Java String
						// Set the attribute value in the Tank
						tank.setModel( encoder.toString( theAttributes.get(attributeHandle) ) );
					}
				}
				return tank;
			}
		}
		return null;
	}

	
	public void updatePosition() throws Exception {
		for ( TankObject tank : instances  ) {
			tank.update();
			AttributeHandleValueMap attributes = rtiamb.getAttributeHandleValueMapFactory().create(1);
			attributes.put( positionHandle, encoder.encodePosition( tank.getPosition() ) );			
			rtiamb.updateAttributeValues( tank.getHandle(), attributes, "Tank Position".getBytes() );
		}
	}
	
	// Here you will send to the RTI the attribute changes of all Tanks
	public void updateAttributeValues() throws Exception {
		// I will send updates for all Tanks 
		for ( TankObject tank : instances  ) {
			
			// Convert Java String to the RTI String 
			HLAunicodeString modelValue = encoder.createHLAunicodeString( tank.getModel() );
			HLAunicodeString nameValue = encoder.createHLAunicodeString( tank.getName() );
			HLAunicodeString serialValue = encoder.createHLAunicodeString( tank.getSerial() );
			HLAunicodeString imageNameValue = encoder.createHLAunicodeString( tank.getImageName() );
			HLAinteger32BE unitTypeValue = encoder.createHLAinteger32BE( tank.getUnitType() );
			
			// Create a package to send all to the RTI
			// We will reserve space for one element ( create(1) ) but it may grow
			// if more is added.
			AttributeHandleValueMap attributes = rtiamb.getAttributeHandleValueMapFactory().create(4);
			// We must tell the attribute handle of this attribute value so the RTI can identify it.
			attributes.put( modelHandle, modelValue.toByteArray() );
			attributes.put( nameHandle, nameValue.toByteArray() );
			attributes.put( serialHandle, serialValue.toByteArray() );
			attributes.put( imageNameHandle, imageNameValue.toByteArray() );
			attributes.put( unitTypeHandle, unitTypeValue.toByteArray() );			
			attributes.put( positionHandle, encoder.encodePosition( tank.getPosition() ) );			
			
			// When send the attributes to update, we must tell the RTI what specific object is it owner.
			// we must give the object handle of this Tank. We can send a tag to track this operation
			rtiamb.updateAttributeValues( tank.getHandle(), attributes, "Tank First Update".getBytes() );
		}
		
	}
	
	// Check if a given object handle is one of mine objects
	// ( a handle of a Tank object )
	public boolean isATank( ObjectInstanceHandle objHandle ) {
		for ( TankObject tankObj : instances  ) {
			if ( tankObj.getHandle().equals( objHandle ) ) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isClassOf( ObjectClassHandle classHandle ) {
		return tankHandle.equals( classHandle );
	}
	
	public ObjectClassHandle getClassHandle() {
		return tankHandle;
	}
	
	public TankClass( RTIambassador rtiamb ) throws Exception {
		// Get the RTIAmbassador. 
		this.rtiamb = rtiamb;
		// Ask the RTI for the class handle of our Tank.
		this.tankHandle = rtiamb.getObjectClassHandle( "HLAobjectRoot.BasicUnit.Tank" );
		// Get the class handle for the Model attribute of the Tank
		this.modelHandle = rtiamb.getAttributeHandle( tankHandle, "Model" );
		this.nameHandle = rtiamb.getAttributeHandle( tankHandle, "Name" );
		this.serialHandle = rtiamb.getAttributeHandle( tankHandle, "Serial" );
		this.imageNameHandle = rtiamb.getAttributeHandle( tankHandle, "ImageName" );
		this.positionHandle = rtiamb.getAttributeHandle( tankHandle, "Position" );
		this.unitTypeHandle = rtiamb.getAttributeHandle( tankHandle, "UnitType" );
		// Create a list to store all attribute handles of the Tank
		// just to avoid to create again when publish / subscribe
		// but you may want to publish and subscribe to different attributes.
		// Why? Because this Java Class ( TankClass.java ) can be used
		// in others Federates to subscribe to Tank attributes and to store
		// discovered Tanks.
		// Ex: A Radar Federate to subscribe to Tank attributes and found
		// some Tanks around.
		this.attributes = rtiamb.getAttributeHandleSetFactory().create();
		attributes.add( modelHandle );
		attributes.add( nameHandle );
		attributes.add( serialHandle );
		attributes.add( imageNameHandle );
		attributes.add( unitTypeHandle );
		attributes.add( positionHandle );
		
		
		// Our Tank list ( created by us or discovered )
		instances = new ArrayList<TankObject>();
		// Our encoder helper.
		encoder = new EncoderDecoder();
	}
	
	// Publish all Tank attributes you want ( the Tank Federate will do this ).
	public void publish() throws RTIexception {
		rtiamb.publishObjectClassAttributes( tankHandle, attributes );
	}
	
	// If you are not the Tank Federate, you can subscribe to the Tank attributes.
	public void subscribe() throws RTIexception {
		rtiamb.subscribeObjectClassAttributes( tankHandle, attributes );		
	}

	
	
}
