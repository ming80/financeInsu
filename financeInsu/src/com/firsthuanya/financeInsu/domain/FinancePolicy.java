package com.firsthuanya.financeInsu.domain;

import java.math.BigDecimal;
import java.util.Date;

public class FinancePolicy extends Policy{
	private RemarkInfo remarkInfo;	//核销
	private SettlementInfo settlementInfo;	//结算
	
	private FinancePolicy(){
		
	}
	
	public FinancePolicy(String policyNo,
			   			 String brokerNo,
			   			 BigDecimal premium,
			   			 BigDecimal cost,
					     String insuranceCompany,
					     String customer,
					     Date inputDate,
					     BigDecimal factorage,
					     double factorageRate,
					     String insuranceType,
					     String invoiceNo,
					     Date importDate,
					     RemarkInfo remarkInfo,
					     SettlementInfo settlementInfo){
		
		super(policyNo,brokerNo,premium,cost,insuranceCompany,customer,
				inputDate,factorage,factorageRate,insuranceType,invoiceNo,importDate);
		
		this.remarkInfo = remarkInfo;
		this.settlementInfo = settlementInfo;				
	}
	
	public RemarkInfo getRemarkInfo() {
		return remarkInfo;
	}
	
	public SettlementInfo getSettlementInfo() {
		return settlementInfo;
	}
		
	//是否已核销
	public boolean hasRemarked(){
		return remarkInfo != null;
	}
	
	//是否已结算
	public boolean hasSettled(){
		return settlementInfo != null;
	}
	
	//核销
	public boolean remark(RemarkInfo info){
		if(info == null)
			throw new IllegalArgumentException("remarkInfo is null!");
		
		if(hasRemarked()) //已核销,不能再次核销
			throw new IllegalStateException("policy is remarked!");
		this.remarkInfo = info;
		return true;
	}
	
	//结算
	public boolean settle(SettlementInfo info){
		if(info == null)
			throw new IllegalArgumentException("settlementInfo is null!");
		
		if(hasSettled())	//已结算,不能再次结算
			throw new IllegalStateException("policy is settled!");	
		this.settlementInfo = info;
		return true;
	}
}
