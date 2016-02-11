package br.com.cmabreu.interceptor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class ClientAccessInterceptor implements Interceptor {
	private static final long serialVersionUID = -2344136157076941239L;
	private Logger logger = LogManager.getLogger( this.getClass().getName() );
	
	public String intercept(ActionInvocation invocation) {
		try {
			return invocation.invoke();
		} catch ( Exception ignored ) {
			return "notLogged";
		}
	}
 
	@Override
	public void destroy() {
		//logger.info("system stop"); 
	}

	
	@Override
	public void init() {
		//logger.info("system init");
	}	
	
}
