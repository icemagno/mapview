package br.com.cmabreu;

import hla.rti1516e.RtiFactoryFactory;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAboolean;
import hla.rti1516e.encoding.HLAfixedRecord;
import hla.rti1516e.encoding.HLAfloat32BE;
import hla.rti1516e.encoding.HLAfloat64BE;
import hla.rti1516e.encoding.HLAinteger32BE;
import hla.rti1516e.encoding.HLAinteger64BE;
import hla.rti1516e.encoding.HLAunicodeString;

class Position {
	private double longitude = 0;
	private double latitude = 0;
	public Position( double longitude, double latitude ) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
}

public class EncoderDecoder {
	private EncoderFactory encoderFactory;
	
	public HLAunicodeString createHLAunicodeString( String value ) {
		return encoderFactory.createHLAunicodeString( value );
	}
	
	public HLAboolean createHLAboolean( boolean value ) {
		return encoderFactory.createHLAboolean( value );
	}
	
	public HLAfloat32BE createHLAfloat32BE( float value ) {
		return encoderFactory.createHLAfloat32BE( value );
	}
	
	public HLAfloat64BE createHLAfloat64BE( double value ) {
		return encoderFactory.createHLAfloat64BE( value );
	}

	public HLAinteger32BE createHLAinteger32BE( int value ) {
		return encoderFactory.createHLAinteger32BE( value );
	}
	
	public HLAinteger64BE createHLAinteger64BE( long value ) {
		return encoderFactory.createHLAinteger64BE( value );
	}
	
	public byte[] encodePosition( Position position ) throws Exception {
		HLAfixedRecord value = encoderFactory.createHLAfixedRecord();
		HLAfloat64BE longitude = encoderFactory.createHLAfloat64BE();
		HLAfloat64BE latitude = encoderFactory.createHLAfloat64BE();

		longitude.setValue( position.getLongitude() );
		latitude.setValue( position.getLatitude() );
		
		value.add(longitude);
		value.add(latitude);
		
		return value.toByteArray();
	}

	public Position decodePosition( byte[] bytes ) throws Exception {
		HLAfixedRecord value = encoderFactory.createHLAfixedRecord();
		HLAfloat64BE longitude = encoderFactory.createHLAfloat64BE();
		HLAfloat64BE latitude = encoderFactory.createHLAfloat64BE();
		value.add(longitude);
		value.add(latitude);
		value.decode(bytes);
		return new Position( longitude.getValue(), latitude.getValue() );
	}

	public String toString ( byte[] bytes )	{
		HLAunicodeString value = encoderFactory.createHLAunicodeString();
		try	{
			value.decode( bytes );
			return value.getValue();
		} catch( DecoderException de )	{
			de.printStackTrace();
			return "";
		}
	}	
	
	public double toFloat64 ( byte[] bytes )	{
		HLAfloat64BE value = encoderFactory.createHLAfloat64BE();
		try	{
			value.decode( bytes );
			return value.getValue();
		} catch( DecoderException de )	{
			de.printStackTrace();
			return -1;
		}
	}
	
	public int toInteger32 ( byte[] bytes )	{
		HLAinteger32BE value = encoderFactory.createHLAinteger32BE();
		try	{
			value.decode( bytes );
			return value.getValue();
		} catch( DecoderException de )	{
			de.printStackTrace();
			return -1;
		}
	}	

	public long toInteger64 ( byte[] bytes )	{
		HLAinteger64BE value = encoderFactory.createHLAinteger64BE();
		try	{
			value.decode( bytes );
			return value.getValue();
		} catch( DecoderException de )	{
			de.printStackTrace();
			return -1;
		}
	}	

	public boolean toBoolean( byte[] bytes ) {
		HLAboolean value = encoderFactory.createHLAboolean();
		try	{
			value.decode( bytes );
		} catch( DecoderException de )	{
			return false;
		}
		return value.getValue();
	}

	
	public EncoderDecoder() throws Exception {
		encoderFactory = RtiFactoryFactory.getRtiFactory().getEncoderFactory();
	}
	
}
