package br.com.cmabreu.units;

public class BaseUnit implements IUnit {
	private String name;
	private String serial;
	private double longitude;
	private double latitude;
	private String imageName = "army";
	private int unitType = UNKNOWN;
	private String featureBasic = "{\"type\":\"Feature\",\"properties\":{\"name\":\"#NAME#\",\"serial\":\"#SERIAL#\","
			+ "\"position\":\"#COORDINATES#\","
			+ "\"bearing\":0,\"color\":\"green\",\"size\":15,\"pin_image\":\"img/pins/#IMG_NAME#.png\"},\"geometry\":{"
			+ "\"type\":\"Point\",\"coordinates\":[#COORDINATES#]}}";

	
	public static final int ME = 100;
	public static final int FRIEND = 101;
	public static final int FOE = 102;
	public static final int UNKNOWN = 103;
	
	
	@Override
	public String getFeature() {
		return featureBasic.replace("#IMG_NAME#", getImageName() ).replace("#NAME#", getName())
			.replace("#SERIAL#", getSerial() ).replace("#COORDINATES#", getCoordinates() );
	}
	
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public void setSerial(String serial) {
		this.serial = serial;
	}
	
	@Override
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	@Override
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getCoordinates() {
		String coo = String.valueOf( longitude ) + "," + String.valueOf( latitude );
		return coo;
	}

	@Override
	public String getSerial() {
		return serial;
	}

	@Override
	public String getImageName() {
		String folder = "signs";
		switch ( unitType ) {
			case ME : folder = "me"; break;
			case FRIEND : folder = "friend"; break;
			case FOE : folder = "foe"; break;
		}
		return folder + "/" + this.imageName;
	}

	@Override
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	@Override
	public int getUnitType() {
		return unitType;
	}
	
	@Override
	public void setUnitType(int unitType) {
		this.unitType = unitType;
	}
}
