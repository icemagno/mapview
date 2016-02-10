package br.cefetrj.sagitarii.units;

public class BaseUnit implements IUnit {
	private String name;
	private String serial;
	private String imageName;
	private double longitude = -50.06542968749966;
	private double latitude = -23.749149728383717;
	private String featureBasic = "[{\"type\":\"Feature\",\"properties\":{\"name\":\"#NAME#\",\"serial\":\"#SERIAL#\","
			+ "\"bearing\":0,\"color\":\"green\",\"size\":15,\"pin_image\":\"img/pins/friend/#IMG_NAME#.png\"},\"geometry\":{"
			+ "\"type\":\"Point\",\"coordinates\":[#COORDINATES#]}}]";

	public String getFeature() {
		return featureBasic.replace("#IMG_NAME#", getImageName()).replace("#NAME#", getName())
			.replace("#SERIAL#", getSerial() ).replace("#COORDINATES#", getCoordinates());		
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
