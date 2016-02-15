package br.com.cmabreu.units;

public interface IUnit {
	String getImageName();
	String getName();
	String getCoordinates();
	String getSerial();
	String getFeature();
	void setName(String name);
	void setImageName( String imageName );
	void setSerial(String serial);
	void setLongitude(double longitude);
	void setLatitude(double latitude);
	int getUnitType();
	void setUnitType(int unitType);
}
