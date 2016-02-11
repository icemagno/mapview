package br.com.cmabreu.federation;

import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleSet;
import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.ObjectClassHandle;
import hla.rti1516e.ObjectInstanceHandle;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.encoding.HLAunicodeString;
import hla.rti1516e.exceptions.RTIexception;

import java.util.ArrayList;
import java.util.List;

import br.com.cmabreu.LogProvider;
import br.com.cmabreu.units.IUnit;

public class UnitClass {
	// The RTI Ambassador - So we can talk to the RTI from here.
	private RTIambassador rtiamb;
	// We must hold the handle of this class 
	private ObjectClassHandle unitHandle;
	// A list of Unit objects we will instantiate
	private List<UnitObject> instances;
	// An encoder helper
	private EncoderDecoder encoder;
	// We must hold the handle for all attributes of this class
	private AttributeHandle nameHandle;
	private AttributeHandle serialHandle;
	private AttributeHandle positionHandle;
	// Hold all our registered attributes  
	private AttributeHandleSet attributes;

	private void log( String message ) 	{
		LogProvider.getInstance().addLog( message );
		System.out.println( "> " + message );
	}
	
	// Cast the list to the Unit Interface ( and turn into immutable )
	public List<IUnit> getInstances() {
		List<IUnit> temp = new ArrayList<IUnit>( instances );
		return temp;
	}
	
	// Create a new Unit and register. Store it in our list of objects. 
	// This method is used only by the Unit Federate.
	public ObjectInstanceHandle createNew(String name, String serial, String imageName, Position position ) throws RTIexception {
		ObjectInstanceHandle coreObjectHandle = rtiamb.registerObjectInstance( unitHandle );
		UnitObject to = new UnitObject(coreObjectHandle);
		to.setName(name);
		to.setSerial(serial);
		to.setImageName(imageName);
		to.setPosition(position);
		log("New Unit " + to.getName() + " created.");
		instances.add( to );
		return coreObjectHandle;
	}

	public ObjectInstanceHandle createNew( ObjectInstanceHandle coreObjectHandle ) throws RTIexception {
		instances.add( new UnitObject(coreObjectHandle) );
		return coreObjectHandle;
	}
	
	// When the Unit Federate sends the updates of its attributes, we must search
	// in our list of discovered Units to see if this specific Unit is in there.
	// If so, update its attributes. Do this only if you are NOT the Unit owner ( since you
	// haven't subscribed to your own attribute changes! )
	public UnitObject update( AttributeHandleValueMap theAttributes, ObjectInstanceHandle theObject ) throws Exception {
		// Find the Unit instance
		for ( UnitObject unit : instances ) {
			if( unit.getHandle().equals( theObject) ) {
				// Update its attributes.
				for( AttributeHandle attributeHandle : theAttributes.keySet() )	{
					// Is the attribute the Unit's Model?
					if( attributeHandle.equals( nameHandle) ) {
						unit.setName( encoder.toString( theAttributes.get(attributeHandle) ) );
					}
					if( attributeHandle.equals( serialHandle) ) {
						unit.setSerial( encoder.toString( theAttributes.get(attributeHandle) ) );
					}
					if( attributeHandle.equals( positionHandle) ) {
						unit.setPosition( encoder.decodePosition( theAttributes.get(attributeHandle) ) );
					}
				}
				return unit;
			}
		}
		return null;
	}
	
	// Here you will send to the RTI the attribute changes of all Units
	// or some Units if you want. Do this only if you are the Unit Federate ( Unit's owner )
	public void updateAttributeValues() throws Exception {
		// I will send updates for all Units 
		for ( UnitObject unit : instances  ) {
			// Convert Java String to the RTI String 
			HLAunicodeString nameValue = encoder.createHLAunicodeString( unit.getName() );
			HLAunicodeString serialValue = encoder.createHLAunicodeString( unit.getSerial() );
			// Create a package to send all to the RTI
			// We will reserve space for one element ( create(1) ) but it may grow
			// if more is added.
			AttributeHandleValueMap attributes = rtiamb.getAttributeHandleValueMapFactory().create(3);
			// We must tell the attribute handle of this attribute value so the RTI can identify it.
			attributes.put( nameHandle, nameValue.toByteArray() );
			attributes.put( serialHandle, serialValue.toByteArray() );
			attributes.put( positionHandle, encoder.encodePosition( unit.getPosition() ) );
			// When send the attributes to update, we must tell the RTI what specific object is it owner.
			// we must give the object handle of this Unit. We can send a tag to track this operation
			rtiamb.updateAttributeValues( unit.getHandle(), attributes, unit.getName().getBytes() );
		}
		
	}
	
	// Check if a given object handle is one of mine objects
	// ( a handle of a Unit object )
	public boolean isAUnit( ObjectInstanceHandle objHandle ) {
		for ( UnitObject unitObj : instances  ) {
			if ( unitObj.getHandle().equals( objHandle ) ) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isClassOf( ObjectClassHandle classHandle ) {
		return unitHandle.equals( classHandle );
	}
	
	public ObjectClassHandle getClassHandle() {
		return unitHandle;
	}
	
	public UnitClass( RTIambassador rtiamb ) throws Exception {
		// Get the RTIAmbassador. 
		this.rtiamb = rtiamb;
		// Ask the RTI for the class handle of our Unit.
		this.unitHandle = rtiamb.getObjectClassHandle( "HLAobjectRoot.BasicUnit" );
		// Get the class handle for all attributes of the Unit
		this.nameHandle = rtiamb.getAttributeHandle( unitHandle, "Name" );
		this.serialHandle = rtiamb.getAttributeHandle( unitHandle, "Serial" );
		this.positionHandle = rtiamb.getAttributeHandle( unitHandle, "Position" );

		this.attributes = rtiamb.getAttributeHandleSetFactory().create();
		attributes.add( nameHandle );
		attributes.add( serialHandle );
		attributes.add( positionHandle );
		// Our Unit list ( created by us or discovered )
		instances = new ArrayList<UnitObject>();
		// Our encoder helper.
		encoder = new EncoderDecoder();
	}
	
	// Publish all Unit attributes you want ( the Unit Federate will do this ).
	public void publish() throws RTIexception {
		log("Published");
		rtiamb.publishObjectClassAttributes( unitHandle, attributes );
	}
	
	// If you are not the Unit Federate, you can subscribe to the Unit attributes.
	public void subscribe() throws RTIexception {
		log("Subscribed");
		rtiamb.subscribeObjectClassAttributes( unitHandle, attributes );		
	}

	
	
}