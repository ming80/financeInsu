package com.firsthuanya.financeInsu.domain;

import java.util.Date;

public class SettlementInfo implements Comparable<SettlementInfo>{
	private Date operationDate;	//��������
	private User operator;	//������Ա
	
	private SettlementInfo(){		
	}
	
	public SettlementInfo(Date operationDate,User operator){
		if(operationDate == null)
			throw new IllegalArgumentException("operationDate is null!");
		if(operator == null)
			throw new IllegalArgumentException("operator is null!");
		
		this.operationDate = operationDate;
		this.operator = operator;		
	}
	
	public Date getOperationDate(){
		return operationDate;
	}
	
	public User getOperator(){
		return operator;
	}

	@Override
	public int compareTo(SettlementInfo o) {
		return this.getOperationDate().compareTo(o.getOperationDate());
	}
}
