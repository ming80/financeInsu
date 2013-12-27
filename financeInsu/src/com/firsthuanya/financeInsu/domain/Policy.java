package com.firsthuanya.financeInsu.domain;

import java.math.BigDecimal;
import java.util.Date;

public class Policy {
	private String policyNo;	//保单号
	private String brokerNo;	//业务员编号
	private BigDecimal premium;	//总保费
	private BigDecimal cost;	//成本
	private String insuranceCompany;	//保险公司
	private String insured;	//客户全称
	private Date inputDate;		//输单日期
	private BigDecimal factorage;	//手续费
	private double factorageRate;	//手续费率
	private String insuranceType;	//险种类别
	private String invoiceNo;	//开票号
	private Date importDate;	//导入日期
	
	protected Policy(){		
	}
	
	public Policy(String policyNo,
				   String brokerNo,
				   BigDecimal premium,
				   BigDecimal cost,
				   String insuranceCompany,
				   String insured,
				   Date inputDate,
				   BigDecimal factorage,
				   double factorageRate,
				   String insuranceType,
				   String invoiceNo,
				   Date importDate){
		
		if(policyNo == null || policyNo.trim().equals(""))
			throw new IllegalArgumentException("");
		if(brokerNo == null || brokerNo.trim().equals(""))
			throw new IllegalArgumentException("");		
		if(premium == null)
			throw new IllegalArgumentException("");
		if(cost == null)
			throw new IllegalArgumentException("");
		if(insuranceCompany == null || insuranceCompany.trim().equals(""))
			throw new IllegalArgumentException("");
		if(insured == null || insured.trim().equals(""))
			throw new IllegalArgumentException("");
		if(inputDate == null)
			throw new IllegalArgumentException("");
		if(factorage == null)
			throw new IllegalArgumentException("");
		if(insuranceType == null || insuranceType.trim().equals(""))
			throw new IllegalArgumentException("");
		if(invoiceNo == null || invoiceNo.trim().equals(""))
			throw new IllegalArgumentException("");
		if(importDate == null)
			throw new IllegalArgumentException("");
		
		this.policyNo = policyNo;
		this.brokerNo = brokerNo;
		this.premium = premium;
		this.cost = cost;
		this.insuranceCompany = insuranceCompany;
		this.insured = insured;
		this.inputDate = inputDate;
		this.factorage = factorage;
		this.factorageRate = factorageRate;
		this.insuranceType = insuranceType;
		this.invoiceNo = invoiceNo;
		this.importDate = importDate;
		
	}
	
	public String getPolicyNo() {
		return policyNo;
	}
	
	public String getBrokerNo() {
		return brokerNo;
	}
	
	public BigDecimal getPremium() {
		return premium;
	}
	
	public BigDecimal getCost() {
		return cost;
	}
	
	public String getInsuranceCompany() {
		return insuranceCompany;
	}
	
	public String getInsured() {
		return insured;
	}
	
	public Date getImportDate() {
		return importDate;
	}
	public BigDecimal getFactorage() {
		return factorage;
	}
	
	public double getFactorageRate() {
		return factorageRate;
	}
	
	public String getInsuranceType() {
		return insuranceType;
	}
	
	public String getInvoiceNo() {
		return invoiceNo;
	}

	public Date getInputDate() {
		return inputDate;
	}
	
	public static Policy getPolicy(){
		return new Policy();
	}
	
	public String toString(){
		return "policyNo:" + this.policyNo + "\n" +
				"brokerNo:" + this.brokerNo + "\n" +
				"premium:" + this.premium + "\n" +
				"cost:" + this.cost + "\n" +
				"insuranceCompany:" + this.insuranceCompany + "\n" +
				"insured:" + this.insured + "\n" +
				"inputDate:" + this.inputDate + "\n" +
				"factorage:" + this.factorage + "\n" +
				"factorageRate:" + this.factorageRate + "\n" +
				"insuranceType:" + this.insuranceType + "\n" +
				"invoiceNo:" + this.invoiceNo + "\n" +
				"importDate:" + this.importDate + "\n";

	}
}
