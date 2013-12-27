package com.firsthuanya.financeInsu.action;

import java.util.Map;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

public class AuthorizationInterceptor extends MethodFilterInterceptor{
	private static Logger logger = Logger.getLogger(AuthorizationInterceptor.class);

	@Override
	protected String doIntercept(ActionInvocation invocation) throws Exception {
		logger.debug("do AuthorizationInterceptor...");
		
		Map<String,Object> session = invocation.getInvocationContext().getSession();
		Object user = session.get("user");
		
		if(user == null){
			session.clear();
			return Action.LOGIN;
		}			
		
//		logger.debug(((Employee)user).getName() + " was authorized!");
		return invocation.invoke();
	}

}
