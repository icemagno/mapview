package br.cefetrj.sagitarii;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class GridConfig {
	// Map initial config
	private int initialZoom;
	private int refreshUnits;
	private int logRefreshInterval;
	private String gpmSourceURL;

	public String getGpmSourceURL() {
		return gpmSourceURL;
	}

	public void setGpmSourceURL(String gpmSourceURL) {
		this.gpmSourceURL = gpmSourceURL;
	}
	
	public int getInitialZoom() {
		return initialZoom;
	}


	public void setInitialZoom(int initialZoom) {
		this.initialZoom = initialZoom;
	}


	public int getRefreshUnits() {
		return refreshUnits;
	}


	public void setRefreshUnits(int refreshUnits) {
		this.refreshUnits = refreshUnits;
	}


	public int getLogRefreshInterval() {
		return logRefreshInterval;
	}


	public void setLogRefreshInterval(int logRefreshInterval) {
		this.logRefreshInterval = logRefreshInterval;
	}


	private String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
        Node nValue = (Node) nlList.item(0);
		return nValue.getNodeValue();
	 }
	

	public void loadConfig( String file ) {
		try {
			InputStream is = this.getClass().getClassLoader().getResourceAsStream(file);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(is);
			doc.getDocumentElement().normalize();
			
			NodeList mapconfig = doc.getElementsByTagName("cim");
			Node mpconfig = mapconfig.item(0);
			Element mpElement = (Element) mpconfig;
			
			setGpmSourceURL(getTagValue("gpmURL", mpElement) );
			setInitialZoom(  Integer.parseInt( getTagValue("initialzoom", mpElement) )  );
			setRefreshUnits(Integer.parseInt( getTagValue("refreshUnits", mpElement) )  );
			setLogRefreshInterval(Integer.parseInt( getTagValue("refreshLog", mpElement) )  );
			
	
			System.out.println("------------------------------------------------");
			System.out.println("The Grid CIM Configuration");
			System.out.println("------------------------------------------------");
			System.out.println("File : " + file);
			System.out.println(" > GPM URL       : " + getGpmSourceURL() );
			System.out.println("MAP : ");
			System.out.println(" > Initial Zoom  : " + getInitialZoom() );
			System.out.println(" > Refresh Units : " + getRefreshUnits() );
			System.out.println("------------------------------------------------");
			
			
		  } catch (Exception e) {
				e.printStackTrace();
		  }			
	}
	
	
}
