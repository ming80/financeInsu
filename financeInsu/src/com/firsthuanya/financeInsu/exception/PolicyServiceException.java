package com.firsthuanya.financeInsu.exception;

public class PolicyServiceException extends RuntimeException{
	public PolicyServiceException(String str){
		super(str);
	}
	
	public PolicyServiceException(String str,Exception e){
		super(str,e);
	}
}
