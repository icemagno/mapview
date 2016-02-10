package br.cefetrj.sagitarii;

import java.util.ArrayList;
import java.util.List;

import br.cefetrj.sagitarii.units.BaseUnit;
import br.cefetrj.sagitarii.units.IUnit;

public class UnitListProvider {
	private static UnitListProvider instance;
	private List<IUnit> list = new ArrayList<IUnit>();
	private String jsonBasic = "{\"type\":\"FeatureCollection\",\"features\":#FEATURES#}";
	
	public void addUnit( IUnit unit ) {
		list.add( unit );
	}
	
	public String asJson() {
		StringBuilder sb = new StringBuilder();
		for( IUnit unit : list ) {
			String value = unit.getFeature();
			sb.append( value );
		}
		return jsonBasic.replace("#FEATURES#", sb.toString() );
	}
	
	private void testUnitCreation() {
		IUnit test1 = new BaseUnit("name", "serial", "army");
		list.add( test1 );
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
