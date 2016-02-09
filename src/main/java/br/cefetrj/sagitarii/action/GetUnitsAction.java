package br.cefetrj.sagitarii.action;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.StrutsStatics;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ActionContext;

@Action(value="getUnits", results= {  
	    @Result(name="ok", type="httpheader", params={"status", "200"}) } 
)   


@ParentPackage("default")
public class GetUnitsAction  {
	
	public String execute(){
		
		String resposta = "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"properties\":{\"name\":\"TRON-02\",\"serial\":\"TRON002\","
				+ "\"bearing\":0,\"color\":\"green\",\"size\":15,\"pin_image\":\"img/pins/friend/army.png\"},\"geometry\":{"
				+ "\"type\":\"Point\",\"coordinates\":[-50.06542968749966,-23.749149728383717]}}]}";
		
		System.out.println( resposta );
		
		try { 
			HttpServletResponse response = (HttpServletResponse)ActionContext.getContext().get(StrutsStatics.HTTP_RESPONSE);
			response.setCharacterEncoding("UTF-8"); 
			response.getWriter().write(resposta);  
		} catch (IOException ex) {  
			System.out.println("[GetUnitsAction] Erro respondendo AJAX."); 
		}
		
		return "ok";
	}
		 

}
