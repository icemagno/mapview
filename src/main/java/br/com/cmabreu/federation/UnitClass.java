package br.com.cmabreu.federation;

import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleSet;
import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.ObjectClassHandle;
import hla.rti1516e.ObjectInstanceHandle;
import hla.rti1516e.RTIambassador;
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
	private AttributeHandle imageNameHandle;
	private AttributeHandle positionHandle;
	private AttributeHandle unitTypeHandle;
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
	
	public ObjectInstanceHandle createNew(ObjectClassHandle classHandle, ObjectInstanceHandle coreObjectHandle ) throws RTIexception {
		UnitObject uo = new UnitObject(coreObjectHandle);
		instances.add( uo );
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
					if( attributeHandle.equals( imageNameHandle) ) {
						unit.setImageName( encoder.toString( theAttributes.get(attributeHandle) ) );
					}
					if( attributeHandle.equals( unitTypeHandle ) ) {
						unit.setUnitType( encoder.toInteger32( theAttributes.get(attributeHandle) ) );
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
		this.imageNameHandle = rtiamb.getAttributeHandle( unitHandle, "ImageName" );
		this.positionHandle = rtiamb.getAttributeHandle( unitHandle, "Position" );
		this.unitTypeHandle = rtiamb.getAttributeHandle( unitHandle, "UnitType" );

		this.attributes = rtiamb.getAttributeHandleSetFactory().create();
		attributes.add( nameHandle );
		attributes.add( serialHandle );
		attributes.add( imageNameHandle );
		attributes.add( positionHandle );
		attributes.add( unitTypeHandle );		
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
