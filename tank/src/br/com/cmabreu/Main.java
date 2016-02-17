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
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class Main implements IKeyReaderObserver {
	private RTIambassador rtiamb;
	private FederateAmbassador fedamb;  

	private TankClass tankClass;

	private void log( String message ) 	{
		System.out.println( "> " + message );
	}
	
	public TankClass getTankClass() {
		return tankClass;
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
				(new File("foms/HLAstandardMIM.xml")).toURI().toURL(),
				(new File("foms/unit.xml")).toURI().toURL()				
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
			(new File("foms/tank.xml")).toURI().toURL()
		};
		rtiamb.joinFederationExecution( federateName, "TankFederateType", 
				federationName, joinModules );   
		log( "Joined Federation as " + federateName );
	}
	
	@Override
	public void notify( String key ) {
		if ( key.equals("n") ) {
			createTank();
			return;
		}
	}
	
	@Override
	public void whenIdle() {
		// Update only the position
		try {
			tankClass.updatePosition();
			rtiamb.evokeMultipleCallbacks(0.1, 0.3);
		} catch ( Exception e ) {
			//
		}
	}	
	
	private void createTank() {
		Random random = new Random();
		double lon = -60.065429 + ( random.nextInt(5)+1 / 100 );
		double lat = -27.74914 + ( random.nextInt(5)+1 / 100 );
		Position p = new Position( lon,lat);
		String id = UUID.randomUUID().toString().substring(0,5).toUpperCase();
		try {
			tankClass.createNew(id,id, p, TankObject.FRIEND );
		} catch ( Exception e ) {
			log("Error when creating a new Tank: " + e.getMessage() );
		}
	}
	
	// Run the Federate.
	public void runFederate() throws Exception	{
		String serial = UUID.randomUUID().toString().substring(0,5).toUpperCase();
		
		createRtiAmbassador();
		connect();
		createFederation("BasicFederation");
		joinFederation("BasicFederation", "TankFederate" + serial);
		
		// Start our objects managers.
		tankClass = new TankClass( rtiamb );
		
		// Publish and subscribe
		publishAndSubscribe();
		
		// Update all attributes for the fisrt time
		// You can push it all immediately or use provideAttributeValueUpdate() 
		// and requestAttributeValueUpdate().   
		// tankClass.updateAttributeValues();
		
		System.out.println("====== TANK FEDERATE ======");
		System.out.println("Type:");
		System.out.println("");
		System.out.println(" n + ENTER : New tank");
		System.out.println(" q + ENTER : Quit");
		System.out.println("");
		KeyReader kr = new KeyReader( this, "q" );
		kr.readKeyUntilQuit();

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
		
		tankClass.publish();
		log( "Published" );
		
	}

	// This is ... ahn... the main method?
	public static void main( String[] args ) {
		if ( args.length > 0  ) {
			Map<String, String> newenv = new HashMap<String, String>();
			newenv.put("RTI_RID_FILE", "./rti.RID" );
		}
		
		try	{
			new Main( ).runFederate();
		} catch( Exception rtie ) {
			rtie.printStackTrace();
		}
	}

	
}