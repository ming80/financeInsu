package com.firsthuanya.financeInsu.domain;

import java.math.BigDecimal;
import java.util.Date;

public class FinancePolicy extends Policy{
	private RemarkInfo remarkInfo;	//����
	private SettlementInfo settlementInfo;	//����
	
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
		
	//�Ƿ��Ѻ���
	public boolean hasRemarked(){
		return remarkInfo != null;
	}
	
	//�Ƿ��ѽ���
	public boolean hasSettled(){
		return settlementInfo != null;
	}
	
	//����
	public boolean remark(RemarkInfo info){
		if(info == null)
			throw new IllegalArgumentException("remarkInfo is null!");
		
		if(hasRemarked()) //�Ѻ���,�����ٴκ���
			throw new IllegalStateException("policy is remarked!");
		this.remarkInfo = info;
		return true;
	}
	
	//����
	public boolean settle(SettlementInfo info){
		if(info == null)
			throw new IllegalArgumentException("settlementInfo is null!");
		
		if(hasSettled())	//�ѽ���,�����ٴν���
			throw new IllegalStateException("policy is settled!");	
		this.settlementInfo = info;
		return true;
	}
}
