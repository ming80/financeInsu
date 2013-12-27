package com.firsthuanya.financeInsu.domain;

import java.math.BigDecimal;
import java.util.Date;

public class Policy {
	private String policyNo;	//������
	private String brokerNo;	//ҵ��Ա���
	private BigDecimal premium;	//�ܱ���
	private BigDecimal cost;	//�ɱ�
	private String insuranceCompany;	//���չ�˾
	private String insured;	//�ͻ�ȫ��
	private Date inputDate;		//�䵥����
	private BigDecimal factorage;	//������
	private double factorageRate;	//��������
	private String insuranceType;	//�������
	private String invoiceNo;	//��Ʊ��
	private Date importDate;	//��������
	
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
