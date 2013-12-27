package com.firsthuanya.financeInsu.action;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.firsthuanya.financeInsu.data.PolicyMapper;
import com.firsthuanya.financeInsu.domain.FinancePolicy;
import com.firsthuanya.financeInsu.domain.Policy;
import com.firsthuanya.financeInsu.domain.RemarkScope;
import com.firsthuanya.financeInsu.domain.SettlementScope;
import com.firsthuanya.financeInsu.service.PolicyService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class UnsettledPolicyQueryAction  extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(UnsettledPolicyQueryAction.class);
	
	private JSONObject results;
    
    private String sort;
    private String order;
    
    private String policyNo;
    private String insured;
    private String brokerNo;
    private String inputDateFrom;
    private String inputDateTo;
    
    private long queryTimestamp;	//时间戳
    
	public String queryPolicy() throws Exception{
		Map<String, Object> session = ActionContext.getContext().getSession();		
		
		Map<String,Object> jsonMap = new HashMap<String,Object>();
		List<FinancePolicy> currentPageRows = doResults(session,jsonMap);		
		
		//排序
		sortResults(currentPageRows,this.sort);		
			
		jsonMap.put("rows", currentPageRows);	//rows为datagrid的属性,表示要显示的记录			
//		jsonMap.put("footer", summarize(results));
		this.setResults(JSONObject.fromObject(jsonMap));
//		System.out.print(jsonMap);
		
		session.put("unsettledPolicies", currentPageRows);
		return SUCCESS;
	}
	
	private boolean isNewQuery(Object policyQueryTimestamp){
		return policyQueryTimestamp == null || (Long)policyQueryTimestamp != this.queryTimestamp;
	}
	
	private List<FinancePolicy> doResults(Map<String, Object> session,Map<String,Object> jsonMap) throws ParseException{		
				
		//如果请求中的时间戳与session中保存的时间戳不一致说明是一次新的查询请求,执行一次查询
		
		if(isNewQuery(session.get("policyQueryTimestamp"))){			
			session.put("policyQueryTimestamp", this.queryTimestamp);
			
			//查询条件设置
			Map<String,Object> queryConditions = new HashMap();
			
			if(!isEmpty(getPolicyNo()))
				queryConditions.put("policyNo", "%" + getPolicyNo().trim() + "%");
			if(!isEmpty(getBrokerNo()))
				queryConditions.put("brokerNo", getBrokerNo().trim());
			if(!isEmpty(getInsured()))
				queryConditions.put("insured", "%" + getInsured().trim() + "%");
			queryConditions.put("settlementScope", SettlementScope.UNSETTLED);
			if(!isEmpty(getInputDateFrom()))
				queryConditions.put("inputDateFrom", parseDate(getInputDateFrom() + " 00:00:00"));
			if(!isEmpty(getInputDateTo()))
				queryConditions.put("inputDateTo", parseDate(getInputDateTo() + " 00:00:00"));
			
			PolicyService service = new PolicyService();
			int count = service.count(queryConditions);
			jsonMap.put("total", count);	//total为datagrid的属性,表示共有多少记录	
			
			//大于1000条记录,说明记录数过多,返回空
			if(count > 1000)				
				return new ArrayList<FinancePolicy>();
						
			//小于1000条记录,返回这些记录
			return service.getPolicies(queryConditions);
		}
		
		List<FinancePolicy> currentPageRows = null;
		if(session.get("unsettledPolicies") != null)
			currentPageRows = (List<FinancePolicy>)session.get("unsettledPolicies");
		else
			currentPageRows =  new ArrayList<FinancePolicy>();
		jsonMap.put("total", currentPageRows.size());
		
		return currentPageRows;	
	}

	//排序
	private void sortResults(List<FinancePolicy> list,final String sort){
		if(this.sort == null || this.sort.trim().equals(""))
			return;
		if(this.order == null || this.order.trim().equals(""))
			return;
		if(list == null || list.size() == 0)
			return;
		
		//排序规则
		Comparator<FinancePolicy> comparator =	new 
			Comparator<FinancePolicy>(){
				@Override
				public int compare(FinancePolicy policy1, FinancePolicy policy2) {
					if("policyNo".equals(sort))
						return doCompare(policy1.getPolicyNo(),policy2.getPolicyNo());
					
					if("brokerNo".equals(sort))
						return doCompare(policy1.getBrokerNo(),policy2.getBrokerNo());
					if("insured".equals(sort))
						return doCompare(policy1.getInsured(),policy2.getInsured());	
					if("premium".equals(sort))
						return doCompare(policy1.getPremium(),policy2.getPremium()); 
					if("cost".equals(sort))
						return doCompare(policy1.getCost(),policy2.getCost()); 
					if("insuranceCompany".equals(sort))
						return doCompare(policy1.getInsuranceCompany(),policy2.getInsuranceCompany()); 
					if("factorageRate".equals(sort))
						return doCompare(policy1.getFactorageRate(),policy2.getFactorageRate()); 
					if("factorage".equals(sort))
						return doCompare(policy1.getFactorage(),policy2.getFactorage()); 
					if("insuranceType".equals(sort))
						return doCompare(policy1.getInsuranceType(),policy2.getInsuranceType()); 
					if("invoiceNo".equals(sort))
						return doCompare(policy1.getInvoiceNo(),policy2.getInvoiceNo());
					if("inputDate".equals(sort))
						return doCompare(policy1.getInputDate(),policy2.getInputDate());
					if("remarkInfo".equals(sort))
						return doCompare(policy1.getRemarkInfo(),policy2.getRemarkInfo());
					if("remarkInfo.operationDate".equals(sort))
						return doCompare(policy1.getRemarkInfo(),policy2.getRemarkInfo());	//RemarkInfo对象本身根据日期排序
					if("settlementInfo".equals(sort))
						return doCompare(policy1.getSettlementInfo(),policy2.getSettlementInfo());
					if("settlementInfo.operationDate".equals(sort))
						return doCompare(policy1.getSettlementInfo(),policy2.getSettlementInfo());	//SettlementInfo对象本身根据日期排序			
					
					return policy1.getPolicyNo().compareTo(policy2.getPolicyNo());
				}
				
				private int doCompare(Comparable comp1,Comparable comp2){
					if(comp1 == null) return -1;
					if(comp2 == null) return 1;
					return comp1.compareTo(comp2);
				}
			};
		
		//根据给出的排序规则升序排列
		if("asc".equals(this.getOrder()))
			Collections.sort(list,comparator);
		//根据给出的排序规则降序排列
		if("desc".equals(this.getOrder()))
			Collections.sort(list,Collections.reverseOrder(comparator));
	}
	
	public JSONObject getResults() {
		return results;
	}

	public void setResults(JSONObject results) {
		this.results = results;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getInsured() {
		return insured;
	}

	public void setInsured(String insured) {
		this.insured = insured;
	}

	public String getBrokerNo() {
		return brokerNo;
	}

	public void setBrokerNo(String brokerNo) {
		this.brokerNo = brokerNo;
	}

	private boolean isEmpty(String str){
		if(str == null) return true;
		if(str.trim().equals("")) return true;
		return false;
	}

	public String getInputDateFrom() {
		return inputDateFrom;
	}

	public void setInputDateFrom(String inputDateFrom) {
		this.inputDateFrom = inputDateFrom;
	}

	public String getInputDateTo() {
		return inputDateTo;
	}

	public void setInputDateTo(String inputDateTo) {
		this.inputDateTo = inputDateTo;
	}
	
	private Date parseDate(String aDate) throws ParseException{
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  //大写H表示24小时显示,否则0点会显示成12
//		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		return sf.parse(aDate);
	}

	public long getQueryTimestamp() {
		return queryTimestamp;
	}

	public void setQueryTimestamp(long queryTimestamp) {
		this.queryTimestamp = queryTimestamp;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}
}
