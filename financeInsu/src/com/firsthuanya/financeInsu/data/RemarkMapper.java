package com.firsthuanya.financeInsu.data;

import com.firsthuanya.financeInsu.domain.FinancePolicy;

public interface RemarkMapper {
	public void insert(FinancePolicy financePolicy); 
	public int count(String policyNo);
}
