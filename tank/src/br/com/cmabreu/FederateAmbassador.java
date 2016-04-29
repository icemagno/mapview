package br.com.cmabreu;

import hla.rti1516e.AttributeHandleSet;
import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.InteractionClassHandle;
import hla.rti1516e.LogicalTime;
import hla.rti1516e.MessageRetractionHandle;
import hla.rti1516e.NullFederateAmbassador;
import hla.rti1516e.ObjectClassHandle;
import hla.rti1516e.ObjectInstanceHandle;
import hla.rti1516e.OrderType;
import hla.rti1516e.ParameterHandleValueMap;
import hla.rti1516e.TransportationTypeHandle;
import hla.rti1516e.FederateAmbassador.SupplementalReceiveInfo;
import hla.rti1516e.FederateAmbassador.SupplementalRemoveInfo;
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
	
	@Override
	public void provideAttributeValueUpdate(ObjectInstanceHandle theObject,
			AttributeHandleSet theAttributes, byte[] userSuppliedTag)
			throws FederateInternalError {

		if ( federate.getTankClass().isATank( theObject ) ) {
			federate.getTankClass().provideAttributeValueUpdate(theObject, theAttributes);
		}
		
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

	@Override
	public void removeObjectInstance(ObjectInstanceHandle arg0, byte[] arg1,
			OrderType arg2, LogicalTime arg3, OrderType arg4,
			SupplementalRemoveInfo arg5) throws FederateInternalError {
		
		System.out.println("  *** removeObjectInstance <<");
	}	
	
	@Override
	public void removeObjectInstance(ObjectInstanceHandle arg0, byte[] arg1,
			OrderType arg2, LogicalTime arg3, OrderType arg4,
			MessageRetractionHandle arg5, SupplementalRemoveInfo arg6)
			throws FederateInternalError {
		
		System.out.println("  *** removeObjectInstance <<");
	}	
	
	@Override
	public void receiveInteraction(InteractionClassHandle interactionClass,	ParameterHandleValueMap theParameters,
			byte[] userSuppliedTag, OrderType sentOrdering,	TransportationTypeHandle theTransport, 
			SupplementalReceiveInfo receiveInfo) throws FederateInternalError {
		
		System.out.println("  *** receiveInteraction");
	}
	
	@Override
	public void removeObjectInstance(ObjectInstanceHandle theObject, byte[] userSuppliedTag, OrderType sentOrdering, SupplementalRemoveInfo removeInfo)	{
		System.out.println("  *** removeObjectInstance ");
	}	
	
	@Override
	public void connectionLost(String arg0) throws FederateInternalError {
		System.out.println("  *** connectionLost ");
	}	
	

}
