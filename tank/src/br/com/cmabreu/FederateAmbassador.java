package br.com.cmabreu;

import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.NullFederateAmbassador;
import hla.rti1516e.ObjectClassHandle;
import hla.rti1516e.ObjectInstanceHandle;
import hla.rti1516e.OrderType;
import hla.rti1516e.TransportationTypeHandle;
import hla.rti1516e.exceptions.FederateInternalError;

public class FederateAmbassador extends NullFederateAmbassador {
	private Main federate;
	
	// The constructor. Must store the Federate to allow 
	// interactivity
	public FederateAmbassador( Main federate ) {
		this.federate = federate;
	}

	
	private void log( String message )	{
		System.out.println( "> " + message );
	}
	
	
	// All new object - rtiamb.registerObjectInstance( classHandle ) -  
	// that arrives into RTI will trigger this event
	@Override
	public void discoverObjectInstance( ObjectInstanceHandle theObject,
	                                    ObjectClassHandle theObjectClass,
	                                    String objectName ) throws FederateInternalError {
		// Is the object we found a kind of Tank?
		if ( federate.getTankClass().isClassOf( theObjectClass ) ) {
			try {
				// If so, create a new Tank object in our list.
				federate.getTankClass().createNew( theObject );
				log("New Tank discovered");
			} catch ( Exception e ) {
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public void reflectAttributeValues( ObjectInstanceHandle theObject,
	                                    AttributeHandleValueMap theAttributes,
	                                    byte[] tag,
	                                    OrderType sentOrder,
	                                    TransportationTypeHandle transport,
	                                    SupplementalReflectInfo reflectInfo ) throws FederateInternalError {
		// Is this attribute from a Tank?
		if ( federate.getTankClass().isATank( theObject ) ) {
			// If so, update my Tank object attributes
			federate.getTankClass().update( theAttributes, theObject );
		}
		
	}



}
