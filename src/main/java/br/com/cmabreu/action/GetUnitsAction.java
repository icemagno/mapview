package br.com.cmabreu.action;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.StrutsStatics;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import br.com.cmabreu.UnitListProvider;

import com.opensymphony.xwork2.ActionContext;

@Action(value="getUnits", results= {  
	    @Result(name="ok", type="httpheader", params={"status", "200"}) } 
)   


@ParentPackage("default")
public class GetUnitsAction  {
	
	public String execute(){
		
		try { 
			String jsonResponse = UnitListProvider.getInstance().asJson();
			
			HttpServletResponse response = (HttpServletResponse)ActionContext.getContext().get(StrutsStatics.HTTP_RESPONSE);
			response.setCharacterEncoding("UTF-8"); 
			response.getWriter().write(jsonResponse);  
		} catch (IOException ex) {  
			System.out.println("[GetUnitsAction] Erro respondendo AJAX."); 
		}
		
		return "ok";
	}
		 

}
