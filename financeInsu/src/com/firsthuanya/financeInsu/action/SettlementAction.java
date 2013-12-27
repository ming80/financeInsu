package com.firsthuanya.financeInsu.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.firsthuanya.financeInsu.domain.FinancePolicy;
import com.firsthuanya.financeInsu.domain.RemarkInfo;
import com.firsthuanya.financeInsu.domain.User;
import com.firsthuanya.financeInsu.service.PolicyService;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class SettlementAction extends ActionSupport{
	private List<String> policyNoList;   
	
	public String execute(){
		Map<String,Object> session = ActionContext.getContext().getSession();
		User user = (User)session.get("user");
		if(session.get("unsettledPolicies") == null)
			return Action.NONE;
					
		List<FinancePolicy> unsettledPolicies = (List<FinancePolicy>)session.get("unsettledPolicies");
		
		List<FinancePolicy> prepareForSettling = new ArrayList();
		for(String policyNo:policyNoList)
			for(FinancePolicy policy:unsettledPolicies)
				if(policy.getPolicyNo().equals(policyNo))
					prepareForSettling.add(policy);		
		
		PolicyService service = new PolicyService();
		service.settle(prepareForSettling, user);
		
		for(FinancePolicy policy:prepareForSettling)
			unsettledPolicies.remove(policy);
		
		
		return Action.SUCCESS;
	}
		
//	public List<String> getPolicyNoList(){
//		return this.policyNoList;
//	}
	
	public void setPolicyNoList(List<String> policyNoList){
		this.policyNoList = policyNoList;
	}
}
