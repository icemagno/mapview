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

public class AircraftClass {
	// The RTI Ambassador - So we can talk to the RTI from here.
	private RTIambassador rtiamb;
	// We must hold the handle of this class 
	private ObjectClassHandle aircraftHandle;
	// A list of objects we will instantiate
	private List<AircraftObject> instances;
	// An encoder helper
	private EncoderDecoder encoder;
	// We must hold the handle for all attributes of this class
	// Just Model for now.
	private AttributeHandle maxRangeHandle;
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
	
	public ObjectInstanceHandle createNew(String name, String serial, Position position, int unitType) throws RTIexception {
		ObjectInstanceHandle coreObjectHandle = rtiamb.registerObjectInstance( aircraftHandle );
		AircraftObject to = new AircraftObject(coreObjectHandle, unitType);
		to.setName(name);
		to.setSerial(serial);
		to.setPosition(position);		
		log("Aircraft " + to.getName() + " created.");
		instances.add( to );
		return coreObjectHandle;
	}

	public ObjectInstanceHandle createNew( ObjectInstanceHandle coreObjectHandle ) throws RTIexception {
		instances.add( new AircraftObject(coreObjectHandle, AircraftObject.UNKNOWN ) );
		return coreObjectHandle;
	}
	
	public AircraftObject update( AttributeHandleValueMap theAttributes, ObjectInstanceHandle theObject ) {
		for ( AircraftObject aircraft : instances ) {
			if( aircraft.isMe( theObject ) ) {
				for( AttributeHandle attributeHandle : theAttributes.keySet() )	{
					if( attributeHandle.equals( maxRangeHandle) ) {
						aircraft.setMaxRange( encoder.toInteger32( theAttributes.get(attributeHandle) ) );
					}
				}
				return aircraft;
			}
		}
		return null;
	}

	
	public void updatePosition() throws Exception {
		for ( AircraftObject aircraft : instances  ) {
			aircraft.update();
			AttributeHandleValueMap attributes = rtiamb.getAttributeHandleValueMapFactory().create(1);
			attributes.put( positionHandle, encoder.encodePosition( aircraft.getPosition() ) );			
			rtiamb.updateAttributeValues( aircraft.getHandle(), attributes, "Aircraft Position".getBytes() );
		}
	}
	
	
	public void provideAttributeValueUpdate(ObjectInstanceHandle theObject,	
			AttributeHandleSet theAttributes )  {
		log("Update attribute request for object " + theObject);
		for ( AircraftObject aircraft : instances ) {
			if( aircraft.isMe( theObject ) ) {
				try {
					updateAttributeValuesObject( aircraft );
				} catch ( Exception e ) {
					log("Error when send attributes updates");
				}
				return;
			}
		}
	}	
	
	/*
	public void updateAttributeValues() throws Exception {
		for ( AircraftObject aircraft : instances  ) {
			updateAttributeValuesObject( aircraft );
		}
	}
	*/

	public void updateAttributeValuesObject( AircraftObject aircraft ) throws Exception {
		HLAinteger32BE maxRangeValue = encoder.createHLAinteger32BE( aircraft.getMaxRange() );
		HLAunicodeString nameValue = encoder.createHLAunicodeString( aircraft.getName() );
		HLAunicodeString serialValue = encoder.createHLAunicodeString( aircraft.getSerial() );
		HLAunicodeString imageNameValue = encoder.createHLAunicodeString( aircraft.getImageName() );
		HLAinteger32BE unitTypeValue = encoder.createHLAinteger32BE( aircraft.getUnitType() );

		AttributeHandleValueMap attributes = rtiamb.getAttributeHandleValueMapFactory().create(4);

		attributes.put( maxRangeHandle, maxRangeValue.toByteArray() );
		attributes.put( nameHandle, nameValue.toByteArray() );
		attributes.put( serialHandle, serialValue.toByteArray() );
		attributes.put( imageNameHandle, imageNameValue.toByteArray() );
		attributes.put( positionHandle, encoder.encodePosition( aircraft.getPosition() ) );			
		attributes.put( unitTypeHandle, unitTypeValue.toByteArray() );			
		
		rtiamb.updateAttributeValues( aircraft.getHandle(), attributes, "Aircraft First Update".getBytes() );
	}

	
	public boolean isAnAircraft( ObjectInstanceHandle objHandle ) {
		for ( AircraftObject aircraftObj : instances  ) {
			if ( aircraftObj.getHandle().equals( objHandle ) ) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isClassOf( ObjectClassHandle classHandle ) {
		return aircraftHandle.equals( classHandle );
	}
	
	public ObjectClassHandle getClassHandle() {
		return aircraftHandle;
	}
	
	public AircraftClass( RTIambassador rtiamb ) throws Exception {
		this.rtiamb = rtiamb;
		this.aircraftHandle = rtiamb.getObjectClassHandle( "HLAobjectRoot.BasicUnit.Aircraft" );

		this.maxRangeHandle = rtiamb.getAttributeHandle( aircraftHandle, "MaxRange" );
		this.nameHandle = rtiamb.getAttributeHandle( aircraftHandle, "Name" );
		this.serialHandle = rtiamb.getAttributeHandle( aircraftHandle, "Serial" );
		this.imageNameHandle = rtiamb.getAttributeHandle( aircraftHandle, "ImageName" );
		this.positionHandle = rtiamb.getAttributeHandle( aircraftHandle, "Position" );
		this.unitTypeHandle = rtiamb.getAttributeHandle( aircraftHandle, "UnitType" );


		this.attributes = rtiamb.getAttributeHandleSetFactory().create();
		attributes.add( maxRangeHandle );
		attributes.add( nameHandle );
		attributes.add( serialHandle );
		attributes.add( imageNameHandle );
		attributes.add( unitTypeHandle );
		attributes.add( positionHandle );
		
		instances = new ArrayList<AircraftObject>();
		encoder = new EncoderDecoder();
	}
	
	public void publish() throws RTIexception {
		rtiamb.publishObjectClassAttributes( aircraftHandle, attributes );
	}
	
	public void subscribe() throws RTIexception {
		rtiamb.subscribeObjectClassAttributes( aircraftHandle, attributes );		
	}

	
	
}
