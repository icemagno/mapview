package br.com.cmabreu.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

@Action (value   =  "MainMap",     results   =  { 
		@Result (location    =  "mainmap.jsp",  name  =  "ok")
	 },  interceptorRefs= { @InterceptorRef("seguranca") }
) 

@ParentPackage("default")
public class MainMapAction {
	
	public String execute ()   {
		return "ok";
	}
	
	
}
