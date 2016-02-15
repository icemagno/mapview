package br.com.cmabreu;
/*
 *   Copyright 2012 The Portico Project
 *
 *   This file is part of portico.
 *
 *   portico is free software; you can redistribute it and/or modify
 *   it under the terms of the Common Developer and Distribution License (CDDL) 
 *   as published by Sun Microsystems. For more information see the LICENSE file.
 *   
 *   Use of this software is strictly AT YOUR OWN RISK!!!
 *   If something bad happens you do not have permission to come crying to me.
 *   (that goes for your lawyer as well)
 *
 */


import hla.rti1516e.CallbackModel;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.ResignAction;
import hla.rti1516e.RtiFactoryFactory;
import hla.rti1516e.exceptions.FederatesCurrentlyJoined;
import hla.rti1516e.exceptions.FederationExecutionAlreadyExists;
import hla.rti1516e.exceptions.FederationExecutionDoesNotExist;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.UUID;

public class Main {
	private RTIambassador rtiamb;
	private FederateAmbassador fedamb;  

	private AircraftClass aircraftClass;

	private void log( String message ) 	{
		System.out.println( "> " + message );
	}
	
	public AircraftClass getAircraftClass() {
		return aircraftClass;
	}
	
	// Just get the RTI Ambassador
	private void createRtiAmbassador() throws Exception {
		log( "Creating RTIambassador." );
		rtiamb = RtiFactoryFactory.getRtiFactory().getRtiAmbassador();
	}

	// Connect our FederateAmbassador to the RTI
	private void connect() throws Exception {
		log( "Connecting..." );
		fedamb = new FederateAmbassador( this );
		rtiamb.connect( fedamb, CallbackModel.HLA_IMMEDIATE );
	}
	
	// Create the Federation. Will raise an error if already created
	// but you can ignore it.
	// We must pass a FOM at this time. I will use the Standard MIM.
	private void createFederation( String federationName ) throws Exception {
		log( "Creating Federation " + federationName );
		try	{
			URL[] modules = new URL[]{
				(new File("foms/HLAstandardMIM.xml")).toURI().toURL()
			};
			rtiamb.createFederationExecution( federationName, modules );
			log( "Created Federation." );
		} catch( FederationExecutionAlreadyExists exists ) {
			log( "Didn't create federation, it already existed." );
		} catch( MalformedURLException urle )	{
			log( "Exception loading one of the FOM modules from disk: " + urle.getMessage() );
			urle.printStackTrace();
			return;
		}
	}

	// Join our Federate to the Federation.
	// We must pass the SOM. Here we will use our SOM file.
	// I recommend you to read this file.
	private void joinFederation( String federationName, String federateName ) throws Exception  {
		URL[] joinModules = new URL[]{
			(new File("foms/unit.xml")).toURI().toURL(),
			(new File("foms/aircraft.xml")).toURI().toURL()
		};
		rtiamb.joinFederationExecution( federateName, "AircraftFederateType", 
				federationName, joinModules );   
		log( "Joined Federation as " + federateName );
	}
	
	// Run the Federate.
	public void runFederate() throws Exception	{
		createRtiAmbassador();
		connect();
		createFederation("BasicFederation");
		joinFederation("BasicFederation", "AircraftFederate");
		
		// Start our objects managers.
		aircraftClass = new AircraftClass( rtiamb );
		
		// Publish and subscribe
		publishAndSubscribe();
		
		// Create and Register 5 Units.
		for ( int x=0; x<2; x++ ) {
			Random random = new Random();
			double lon = -64.265429 + ( random.nextInt(10)+1 / 1000 );
			double lat = -36.64914 + ( random.nextInt(10)+1 / 1000 );
			Position p = new Position( lon,lat);
			String id = UUID.randomUUID().toString().substring(0,5).toUpperCase();
			aircraftClass.createNew(id,id, p, AircraftObject.FOE );
		}
		
		// Update all attributes for the first time
		aircraftClass.updateAttributeValues();
		
		// Wait the user to press a key to exit; 
		System.out.println("Press a key to exit.");
		while ( System.in.available() == 0 ) {
			try {
				// Update only the position
				aircraftClass.updatePosition();
				rtiamb.evokeMultipleCallbacks(0.1, 0.3);
			} catch (Exception e) {
				// 
			}				
		}

		// Get out!
		exitFromFederation();
	}
	
	// Exit from Federation and try to destroy it.
	// delete all objects owned by this Federate.
	private void exitFromFederation() throws Exception {
		rtiamb.resignFederationExecution( ResignAction.DELETE_OBJECTS );
		log( "Resigned from Federation" );
		try	{
			rtiamb.destroyFederationExecution( "BasicFederation" );
			log( "Destroyed Federation" );
		} catch( FederationExecutionDoesNotExist dne ) {
			log( "No need to destroy federation, it doesn't exist" );
		} catch( FederatesCurrentlyJoined fcj ){
			log( "Didn't destroy federation, federates still joined" );
		}
	}
	
	// To publish our attributes and subscribe to interactions 
	// and other Federates attributes
	private void publishAndSubscribe() throws Exception	{
		
		aircraftClass.publish();
		log( "Published" );
		
	}

	// This is ... ahn... the main method?
	public static void main( String[] args ) {
		try	{
			new Main( ).runFederate();
		} catch( Exception rtie ) {
			rtie.printStackTrace();
		}
	}

	
}