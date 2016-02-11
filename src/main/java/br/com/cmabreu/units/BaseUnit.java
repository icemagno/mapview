package br.com.cmabreu.units;

public class BaseUnit implements IUnit {
	private String name;
	private String serial;
	private String imageName;
	private double longitude;
	private double latitude;
	private String featureBasic = "{\"type\":\"Feature\",\"properties\":{\"name\":\"#NAME#\",\"serial\":\"#SERIAL#\","
			+ "\"position\":\"#COORDINATES#\","
			+ "\"bearing\":0,\"color\":\"green\",\"size\":15,\"pin_image\":\"img/pins/friend/#IMG_NAME#.png\"},\"geometry\":{"
			+ "\"type\":\"Point\",\"coordinates\":[#COORDINATES#]}}";

	public String getFeature() {
		return featureBasic.replace("#IMG_NAME#", getImageName()).replace("#NAME#", getName())
			.replace("#SERIAL#", getSerial() ).replace("#COORDINATES#", getCoordinates());		
	}
	
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public BaseUnit( String name, String serial, String imageName  ) {
		this.imageName = imageName;
		this.name = name;
		this.serial = serial;
	}
	
	@Override
	public String getImageName() {
		return imageName;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getCoordinates() {
		longitude = longitude + 0.001;
		String coo = String.valueOf( longitude ) + "," + String.valueOf( latitude );
		return coo;
	}

	@Override
	public String getSerial() {
		return serial;
	}

}
