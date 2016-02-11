package br.com.cmabreu;

import java.util.ArrayList;
import java.util.List;

import br.com.cmabreu.units.BaseUnit;
import br.com.cmabreu.units.IUnit;

public class UnitListProvider {
	private static UnitListProvider instance;
	private List<IUnit> list = new ArrayList<IUnit>();
	private String jsonBasic = "{\"type\":\"FeatureCollection\",\"features\":[#FEATURES#]}";
	
	public void addUnit( IUnit unit ) {
		list.add( unit );
	}
	
	public String asJson() {
		StringBuilder sb = new StringBuilder();
		String prefix = "";
		for( IUnit unit : list ) {
			String value = unit.getFeature();
			sb.append( prefix + value );
			prefix = ",";
		}
		return jsonBasic.replace("#FEATURES#", sb.toString() );
	}
	
	private void testUnitCreation() {
		// This is just a test! DELETE IT !
		IUnit test1 = new BaseUnit("Unit 1", "ABCDEF", "army");
		test1.setLongitude( -50.065429 );
		test1.setLatitude( -23.74914 );
		list.add( test1 );
		
		IUnit test2 = new BaseUnit("Unit 2", "HYGTFR", "army");
		test2.setLongitude( -50.36542 );
		test2.setLatitude( -23.84914);
		list.add( test2 );
		
	}
	
	public List<IUnit> getList() {
		return list;
	}
	
	private UnitListProvider() {
		list = new ArrayList<IUnit>();
		testUnitCreation();
	}
	
	public static UnitListProvider getInstance() {
		if ( instance == null ) {
			instance = new UnitListProvider();
		}
		return instance;
	}

}
