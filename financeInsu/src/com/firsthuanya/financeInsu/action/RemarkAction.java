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

public class RemarkAction extends ActionSupport{
	private List<String> policyNoList;
   
	
	public String execute(){
		Map<String,Object> session = ActionContext.getContext().getSession();
		User user = (User)session.get("user");
		if(session.get("unremarkedPolicies") == null)
			return Action.NONE;
					
		List<FinancePolicy> unremarkedPolicies = (List<FinancePolicy>)session.get("unremarkedPolicies");
		
		List<FinancePolicy> prepareForRemarking = new ArrayList();
		for(String policyNo:policyNoList)
			for(FinancePolicy policy:unremarkedPolicies)
				if(policy.getPolicyNo().equals(policyNo))
					prepareForRemarking.add(policy);		
		
		PolicyService service = new PolicyService();
		service.remark(prepareForRemarking, user);
		
		for(FinancePolicy policy:prepareForRemarking)
			unremarkedPolicies.remove(policy);
		
		
		return Action.SUCCESS;
	}
		
//	public List<String> getPolicyNoList(){
//		return this.policyNoList;
//	}
	
	public void setPolicyNoList(List<String> policyNoList){
		this.policyNoList = policyNoList;
	}
}
