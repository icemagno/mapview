package br.cefetrj.sagitarii.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.StrutsStatics;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import br.cefetrj.sagitarii.units.IUnit;

import com.opensymphony.xwork2.ActionContext;

@Action(value="getUnits", results= {  
	    @Result(name="ok", type="httpheader", params={"status", "200"}) } 
)   


@ParentPackage("default")
public class GetUnitsAction  {
	private List<IUnit> list;
	
	public String execute(){
		
		StringBuilder sb = new StringBuilder();
		// -50.06542968749966,-23.749149728383717
		String featureBasic = "[{\"type\":\"Feature\",\"properties\":{\"name\":\"#NAME#\",\"serial\":\"#SERIAL#\","
				+ "\"bearing\":0,\"color\":\"green\",\"size\":15,\"pin_image\":\"img/pins/friend/#IMG_NAME#.png\"},\"geometry\":{"
				+ "\"type\":\"Point\",\"coordinates\":[#COORDINATES#]}}]";
		
		String jsonBasic = "{\"type\":\"FeatureCollection\",\"features\":#FEATURES#}";
		
		for( IUnit unit : list ) {
			String value = featureBasic.replace("#IMG_NAME#", unit.getImageName()).replace("#NAME#", unit.getName())
					.replace("#SERIAL#", unit.getSerial() ).replace("#COORDINATES#", unit.getCoordinates());
			sb.append( value );
		}
		
		String jsonResponse = jsonBasic.replace("#FEATURES#", sb.toString() );
		
		try { 
			HttpServletResponse response = (HttpServletResponse)ActionContext.getContext().get(StrutsStatics.HTTP_RESPONSE);
			response.setCharacterEncoding("UTF-8"); 
			response.getWriter().write(jsonResponse);  
		} catch (IOException ex) {  
			System.out.println("[GetUnitsAction] Erro respondendo AJAX."); 
		}
		
		return "ok";
	}
		 

}
