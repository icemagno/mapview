package br.com.cmabreu.action;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.StrutsStatics;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ActionContext;

@Action(value="getMapConfig", results= {  
	    @Result(name="ok", type="httpheader", params={"status", "200"}) },
	    interceptorRefs= { @InterceptorRef("seguranca") } 
)   

@ParentPackage("default")
public class GetInitialMapConfigAction {

	public String execute(){
		
		String mapCenterLat = "-25";
		String mapCenterLon = "-49";
		
		int zoom = 5;
		int refreshIntval = 2000;
		int logRefreshInterval = 2000;
		
		String resposta = "{\"logRefreshInterval\":\""+logRefreshInterval+"\",\"refreshInterval\":\"" + refreshIntval + "\",\"zoom\":\""+zoom+"\",\"mapCenterLat\":\""+mapCenterLat+"\",\"mapCenterLon\":\""+mapCenterLon+"\"}";
		
		try { 
			HttpServletResponse response = (HttpServletResponse)ActionContext.getContext().get(StrutsStatics.HTTP_RESPONSE);
			response.setCharacterEncoding("UTF-8"); 
			response.getWriter().write(resposta);  
		} catch (IOException ex) {  
			System.out.println("[GetDestinyDetailAction] Erro respondendo AJAX."); 
		}
		
		return "ok";		
	}
	
}
