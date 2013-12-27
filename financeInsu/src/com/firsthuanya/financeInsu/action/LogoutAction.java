package com.firsthuanya.financeInsu.action;

import java.util.Map;

import org.apache.log4j.Logger;

import com.firsthuanya.financeInsu.domain.User;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class LogoutAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(LogoutAction.class);
	
	public String logout() throws Exception{
		Map<String,Object> session = ActionContext.getContext().getSession();
		
		if(session.get("user") != null){
			User user = (User)session.get("user");
			logger.info(user.getName() + " logout!");	
		}			
		
		session.clear();
	
		return SUCCESS;
	}	
}
