package com.firsthuanya.financeInsu.data;

import java.util.List;
import java.util.Map;

import com.firsthuanya.financeInsu.domain.FinancePolicy;
import com.firsthuanya.financeInsu.domain.Policy;

public interface PolicyMapper {
	public void insert(Policy policy);
	public int count(String policyNo);
	public int countPolicies(List<Policy> policies);	//��Щ�����Ƿ����,���ص����ֱ�ʾ���ݿ��д��ڼ�����¼
	public int countPoliciesByCondition(Map<String,Object> conditions);
	public List<FinancePolicy> findPolicies(Map<String,Object> conditions);
	
}
