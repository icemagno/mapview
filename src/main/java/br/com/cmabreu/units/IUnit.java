package br.com.cmabreu.units;

public interface IUnit {
	String getImageName();
	String getName();
	String getCoordinates();
	String getSerial();
	String getFeature();
	void setLongitude(double longitude);
	void setLatitude(double latitude);
}
