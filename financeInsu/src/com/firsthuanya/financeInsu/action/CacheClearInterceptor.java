package com.firsthuanya.financeInsu.action;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.StrutsStatics;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

public class CacheClearInterceptor extends MethodFilterInterceptor{
	private static Logger logger = Logger.getLogger(CacheClearInterceptor.class); 
	
	@Override
	public String doIntercept(ActionInvocation invocation) throws Exception {
		logger.debug("do CacheClearInterceptor...");
		
		HttpServletResponse response = 
				(HttpServletResponse)invocation.getInvocationContext().get(StrutsStatics.HTTP_RESPONSE);
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
	    response.setDateHeader("Expires", -1);
	    
		return invocation.invoke();
	}



}
