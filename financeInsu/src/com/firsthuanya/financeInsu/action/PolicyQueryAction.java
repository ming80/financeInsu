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

import com.firsthuanya.financeInsu.domain.FinancePolicy;
import com.firsthuanya.financeInsu.domain.Policy;
import com.firsthuanya.financeInsu.domain.RemarkScope;
import com.firsthuanya.financeInsu.domain.SettlementScope;
import com.firsthuanya.financeInsu.service.PolicyService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class PolicyQueryAction  extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(PolicyQueryAction.class);
	
	private JSONObject results;
	private long rows;//ÿҳ��ʾ�ļ�¼��,datagrid���ṩ��������    
    private long page;//��ǰ�ڼ�ҳ,datagrid���ṩ��������        
    
    private String sort;
    private String order;
    
    private String policyNo;
    private String insured;
    private String brokerNo;
    private String remarkScope;
    private String settlementScope;
    private String inputDateFrom;
    private String inputDateTo;
    
    private long queryTimestamp;	//ʱ���
    
	public String queryPolicy() throws Exception{		
		Map<String, Object> session = ActionContext.getContext().getSession();
		
		List<FinancePolicy> results = doResults(session);
		
		//����
		sortResults(results,this.sort);
		
		//server�˷�ҳ
		List<FinancePolicy> currentPageRows = new ArrayList<FinancePolicy>();
		Iterator<FinancePolicy> iter = results.iterator();
		for(int index = 0;index < (this.page - 1) * this.rows;index++)
			iter.next();
		for(int i = 0;i < this.rows;i++)
			if(iter.hasNext())
				currentPageRows.add((FinancePolicy)iter.next());
			else
				break;
		
		Map<String,Object> jsonMap = new HashMap<String,Object>();
			
		jsonMap.put("rows", currentPageRows);	//rowsΪdatagrid������,��ʾҪ��ʾ�ļ�¼		
		jsonMap.put("total", results.size());	//totalΪdatagrid������,��ʾ���ж��ټ�¼	
		jsonMap.put("footer", summarize(results));
		this.setResults(JSONObject.fromObject(jsonMap));
//		System.out.print(jsonMap);
		
		session.put("results", results);
		return SUCCESS;
	}
	
	private List<FinancePolicy> doResults(Map<String, Object> session) throws ParseException{		
				
		Long policyQueryTimestamp = (Long)session.get("policyQueryTimestamp");
		//��������е�ʱ�����session�б����ʱ�����һ��˵����һ���µĲ�ѯ����,ִ��һ�β�ѯ
		if(policyQueryTimestamp == null || policyQueryTimestamp != this.queryTimestamp){			
			session.put("policyQueryTimestamp", this.queryTimestamp);
			
			//��ѯ��������
			Map<String,Object> queryConditions = new HashMap();
			
			if(!isEmpty(getPolicyNo()))
				queryConditions.put("policyNo", "%" + getPolicyNo().trim() + "%");
			if(!isEmpty(getBrokerNo()))
				queryConditions.put("brokerNo", getBrokerNo().trim());
			if(!isEmpty(getInsured()))
				queryConditions.put("insured", "%" + getInsured().trim() + "%");
			queryConditions.put("remarkScope", parseRemarkScope(getRemarkScope()));
			queryConditions.put("settlementScope", parseSettlementScope(getSettlementScope()));								 
			if(!isEmpty(getInputDateFrom()))
				queryConditions.put("inputDateFrom", parseDate(getInputDateFrom() + " 00:00:00"));
			if(!isEmpty(getInputDateTo()))
				queryConditions.put("inputDateTo", parseDate(getInputDateTo() + " 00:00:00"));
			
			PolicyService service = new PolicyService();
			List<FinancePolicy> results = service.getPolicies(queryConditions);
			
			return results;
		}
		
		if(session.get("results") != null)
			return (List<FinancePolicy>)session.get("results");
		
		return new ArrayList<FinancePolicy>();
	}

	//����
	private void sortResults(List<FinancePolicy> list,final String sort){
		if(this.sort == null || this.sort.trim().equals(""))
			return;
		if(this.order == null || this.order.trim().equals(""))
			return;
		
		//�������
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
						return doCompare(policy1.getRemarkInfo(),policy2.getRemarkInfo());	//RemarkInfo�����������������
					if("settlementInfo".equals(sort))
						return doCompare(policy1.getSettlementInfo(),policy2.getSettlementInfo());
					if("settlementInfo.operationDate".equals(sort))
						return doCompare(policy1.getSettlementInfo(),policy2.getSettlementInfo());	//SettlementInfo�����������������			
					
					return policy1.getPolicyNo().compareTo(policy2.getPolicyNo());
				}
				
				private int doCompare(Comparable comp1,Comparable comp2){
					if(comp1 == null) return -1;
					if(comp2 == null) return 1;
					return comp1.compareTo(comp2);
				}
			};
		
		//���ݸ��������������������
		if("asc".equals(this.getOrder()))
			Collections.sort(list,comparator);
		//���ݸ������������������
		if("desc".equals(this.getOrder()))
			Collections.sort(list,Collections.reverseOrder(comparator));
	}
	
	public JSONObject getResults() {
		return results;
	}

	public void setResults(JSONObject results) {
		this.results = results;
	}

	public long getRows() {
		return rows;
	}

	public void setRows(long rows) {
		this.rows = rows;
	}

	public long getPage() {
		return page;
	}

	public void setPage(long page) {
		this.page = page;
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

	public String getRemarkScope() {
		return remarkScope;
	}

	public void setRemarkScope(String remarkScope) {
		this.remarkScope = remarkScope;
	}
	
	public String getSettlementScope() {
		return settlementScope;
	}

	public void setSettlementScope(String settlementScope) {
		this.settlementScope = settlementScope;
	}
	
	private RemarkScope parseRemarkScope(String remarkScope){
		if("all".equals(remarkScope))
			return RemarkScope.ALL;
		if("remarked".equals(remarkScope))
			return RemarkScope.REMARKED;
		if("unremarked".equals(remarkScope))
			return RemarkScope.UNREMARKED;
		return null;
	}

	private SettlementScope parseSettlementScope(String settlementScope){
		if("all".equals(settlementScope))
			return SettlementScope.ALL;
		if("settled".equals(settlementScope))
			return SettlementScope.SETTLED;
		if("unsettled".equals(settlementScope))
			return SettlementScope.UNSETTLED;
		return null;
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
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  //��дH��ʾ24Сʱ��ʾ,����0�����ʾ��12
//		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		return sf.parse(aDate);
	}
	
	private List<Map<String,Object>> summarize(List<FinancePolicy> results){	
		if(results == null || results.size() == 0)
			return null;
		
		BigDecimal premiumTotal = new BigDecimal("0");
		BigDecimal costTotal = new BigDecimal("0");
		BigDecimal factorageTotal = new BigDecimal("0");
		
		
		for(Policy e:results){
			if(e.getPremium() != null)
				premiumTotal = premiumTotal.add(e.getPremium());
			if(e.getCost() != null)
				costTotal = costTotal.add(e.getCost());	
			if(e.getFactorage() != null)
				factorageTotal = factorageTotal.add(e.getFactorage());
		}		
		
		Map<String,Object> summary = new HashMap<String,Object>();
		summary.put("policyNo", "�ܼ�");
		summary.put("premium", premiumTotal);
		summary.put("cost", costTotal);	
		summary.put("factorage", factorageTotal);
		
		List<Map<String,Object>> summaries = new ArrayList<Map<String,Object>>();
		summaries.add(summary);
		
		return summaries;
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
