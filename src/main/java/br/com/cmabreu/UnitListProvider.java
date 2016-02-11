package br.com.cmabreu;

import br.com.cmabreu.federation.MapViewFederate;
import br.com.cmabreu.units.IUnit;

public class UnitListProvider {
	private static UnitListProvider instance;
	
	private String jsonBasic = "{\"type\":\"FeatureCollection\",\"features\":[#FEATURES#]}";
	
	public String asJson() {
		StringBuilder sb = new StringBuilder();
		String prefix = "";
		for( IUnit unit : MapViewFederate.getInstance().getUnitClass().getInstances() ) {
			String value = unit.getFeature();
			sb.append( prefix + value );
			prefix = ",";
		}
		return jsonBasic.replace("#FEATURES#", sb.toString() );
	}
	
	public static UnitListProvider getInstance() {
		if ( instance == null ) {
			instance = new UnitListProvider();
		}
		return instance;
	}

}
