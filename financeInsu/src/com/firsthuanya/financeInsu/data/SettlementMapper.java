package com.firsthuanya.financeInsu.data;

import java.util.Date;
import java.util.List;

import com.firsthuanya.financeInsu.domain.FinancePolicy;
import com.firsthuanya.financeInsu.domain.Policy;
import com.firsthuanya.financeInsu.domain.User;

public interface SettlementMapper {
	public void insert(FinancePolicy financePolicy); 
	public int count(String policyNo);
}
