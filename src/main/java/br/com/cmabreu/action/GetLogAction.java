package br.com.cmabreu.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.StrutsStatics;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import br.com.cmabreu.LogProvider;

import com.opensymphony.xwork2.ActionContext;

@Action(value="getLog", results= {  
	    @Result(name="ok", type="httpheader", params={"status", "200"}) },
	    interceptorRefs= { @InterceptorRef("seguranca") } 
)   

@ParentPackage("default")
public class GetLogAction {
	
	public String execute(){	
		
		List<String> log = LogProvider.getInstance().getLog();
		StringBuilder sb = new StringBuilder();
		String prefix = "";
		for ( String s : log) {
			sb.append( prefix +  "{\"type\":\"0\",\"message\":\""+s+"\"}");
			prefix = ",";
		}
		String resposta = "{\"messages\":["+sb.toString()+"]}";
		
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
