package com.firsthuanya.financeInsu.domain;

import java.util.Date;

public class RemarkInfo implements Comparable<RemarkInfo>{	
	private Date operationDate;	//操作日期
	private User operator;	//操作人员
	
	private RemarkInfo(){
		
	}
	
	public RemarkInfo(Date operationDate,User operator){
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
	public int compareTo(RemarkInfo o) {		
		return this.getOperationDate().compareTo(o.getOperationDate());
	}
	
}
