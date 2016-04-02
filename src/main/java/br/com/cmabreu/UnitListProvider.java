package br.com.cmabreu;

import br.com.cmabreu.federation.MapViewFederate;
import br.com.cmabreu.units.IUnit;
import br.com.cmabreu.websockets.ClientList;

public class UnitListProvider implements Runnable {
	private String jsonBasic = "{\"type\":\"FeatureCollection\",\"features\":[#FEATURES#]}";
	
	private String asJson() {
		StringBuilder sb = new StringBuilder();
		String prefix = "";
		for( IUnit unit : MapViewFederate.getInstance().getUnitClass().getInstances() ) {
			String value = unit.getFeature();
			sb.append( prefix + value );
			prefix = ",";
		}
		return jsonBasic.replace("#FEATURES#", sb.toString() );
	}

	@Override
	public void run() {
		try {
			String jsonResponse = asJson();
			ClientList.getInstance().message(jsonResponse);
		} catch ( Exception e ) {
			
		}
	}
	

}
