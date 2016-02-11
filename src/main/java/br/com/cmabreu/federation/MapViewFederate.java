package br.com.cmabreu.federation;
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

import br.com.cmabreu.LogProvider;
import br.com.cmabreu.PathFinder;

public class MapViewFederate {
	private RTIambassador rtiamb;
	private FederateAmbassador fedamb;  
	private static MapViewFederate instance;
	private UnitClass unitClass;
	
	private void log( String message ) 	{
		LogProvider.getInstance().addLog( message );
		System.out.println( "> " + message );
	}

	public static MapViewFederate getInstance() {
		if ( instance == null ) {
			instance = new MapViewFederate();
		}
		return instance;
	}
	
	public UnitClass getUnitClass() {
		return unitClass;
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
				(new File(PathFinder.getInstance().getPath() + "/foms/HLAstandardMIM.xml")).toURI().toURL()
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
			(new File( PathFinder.getInstance().getPath() + "/foms/unit.xml")).toURI().toURL()
		};
		rtiamb.joinFederationExecution( federateName, "MapViewType", 
				federationName, joinModules );   
		log( "Joined Federation as " + federateName );
	}
	
	// Run the Federate.
	public void runFederate() throws Exception	{
		createRtiAmbassador();
		connect();
		createFederation("BasicFederation");
		joinFederation("BasicFederation", "MapView");
		
		// Start our objects managers.
		unitClass = new UnitClass( rtiamb );
		
		// Publish and subscribe
		publishAndSubscribe();
		
	}
	
	// Exit from Federation and try to destroy it.
	// delete all objects owned by this Federate.
	public void exitFromFederation() throws Exception {
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
		unitClass.subscribe();
	}

	public void startFederate() {
		try	{
			runFederate();
		} catch( Exception rtie ) {
			log( rtie.getMessage() );
			rtie.printStackTrace();
		}
	}

	
}