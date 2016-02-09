package br.cefetrj.sagitarii.action;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.StrutsStatics;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ActionContext;

@Action(value="getLog", results= {  
	    @Result(name="ok", type="httpheader", params={"status", "200"}) },
	    interceptorRefs= { @InterceptorRef("seguranca") } 
)   

@ParentPackage("default")
public class GetLogAction {
	
	public String execute(){	
		String parameters = "";
		
		ServletContext context = ServletActionContext.getServletContext();
		
		String resposta = "";
		
		try { 
			HttpServletResponse response = (HttpServletResponse)ActionContext.getContext().get(StrutsStatics.HTTP_RESPONSE);
			response.setCharacterEncoding("UTF-8"); 
			response.getWriter().write(resposta);  
		} catch (IOException ex) {  
			System.out.println("[GetLogAction] Erro respondendo AJAX."); 
		}
		
		return "ok";
	}

}
