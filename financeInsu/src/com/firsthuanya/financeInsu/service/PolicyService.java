package com.firsthuanya.financeInsu.service;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.session.TransactionIsolationLevel;

import com.firsthuanya.financeInsu.data.PolicyMapper;
import com.firsthuanya.financeInsu.data.RemarkMapper;
import com.firsthuanya.financeInsu.data.SettlementMapper;
import com.firsthuanya.financeInsu.domain.FinancePolicy;
import com.firsthuanya.financeInsu.domain.Policy;
import com.firsthuanya.financeInsu.domain.RemarkInfo;
import com.firsthuanya.financeInsu.domain.RemarkScope;
import com.firsthuanya.financeInsu.domain.SettlementInfo;
import com.firsthuanya.financeInsu.domain.SettlementScope;
import com.firsthuanya.financeInsu.domain.User;
import com.firsthuanya.financeInsu.exception.PolicyServiceException;

public class PolicyService {
	private final static String RESOURCE = "com/firsthuanya/financeInsu/data/Configuration.xml";
	
	private SqlSession openSession(ExecutorType type,TransactionIsolationLevel level){
		Reader reader;
		try {
			reader = Resources.getResourceAsReader(RESOURCE);
		} catch (IOException e) {
			throw new PolicyServiceException("open session failed!",e);
		}
		
		SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
		
		if(type != null && level == null)
			return builder.build(reader).openSession(type);
		
		if(type == null && level != null)
			return builder.build(reader).openSession(level);
		
		if(type != null && level != null)
			return builder.build(reader).openSession(type,level);
		
		return builder.build(reader).openSession();
	}	
	
	/**
	 * 添加保单
	 * @param policies 要添加的保单
	 * @return 如果添加成功,返回添加的保单数量,如果失败,则返回0
	 */
	public int addPolicy(List<Policy> policies){
		SqlSession session = null;
		try{
			session = openSession(ExecutorType.BATCH,TransactionIsolationLevel.SERIALIZABLE);	//最高事务隔离级别(事务其间锁住整表)
			
			PolicyMapper policyMapper = session.getMapper(PolicyMapper.class);
			
			//记录已存在,导入失败
			if(policyMapper.countPolicies(policies) > 0)
				return 0;
			
			for(Policy policy:policies)
				policyMapper.insert(policy);
			
			session.commit();
			return policies.size();
		}
		finally{
			if(session != null) session.close();
		}
	}
	
	public boolean hasSuchPolicy(String policyNo){
		SqlSession session = null;
		try{
			session = openSession(null,null);
			
			PolicyMapper mapper = session.getMapper(PolicyMapper.class);
			
			return mapper.count(policyNo) > 0;
		}
		finally{
			if(session != null) session.close();
		}
		
	}
	
	public List<FinancePolicy> getPolicies(Map<String,Object> queryConditions){
		SqlSession session = null;
		try{
			session = openSession(null,null);
			PolicyMapper mapper = session.getMapper(PolicyMapper.class);
			return mapper.findPolicies(queryConditions);
		}
		finally{
			if(session != null) session.close();
		}
	}
	
	public int count(Map<String,Object> queryConditions){
		SqlSession session = null;
		try{
			session = openSession(null,null);
			PolicyMapper mapper = session.getMapper(PolicyMapper.class);
			return mapper.countPoliciesByCondition(queryConditions);
		}
		finally{
			if(session != null) session.close();
		}
	}
	
	public int remark(List<FinancePolicy> policies,User operater){
		SqlSession session = null;
		try {						
			session = openSession(ExecutorType.BATCH,null);			
			RemarkMapper mapper = session.getMapper(RemarkMapper.class);
			
			int insertCount = 0;
			RemarkInfo info = new RemarkInfo(new Date(),operater);
			for(FinancePolicy policy:policies){
				policy.remark(info);
				if(mapper.count(policy.getPolicyNo()) == 0){
					mapper.insert(policy);
					insertCount++;
				}					
			}
			
			session.commit();
			return insertCount;
		} 
		finally{
			if(session != null) session.close();
		}
	}
	
	public int settle(List<FinancePolicy> policies,User operater){
		SqlSession session = null;
		try {						
			session = openSession(ExecutorType.BATCH,null);			
			SettlementMapper mapper = session.getMapper(SettlementMapper.class);
			
			int insertCount = 0;
			SettlementInfo info = new SettlementInfo(new Date(),operater);
			for(FinancePolicy policy:policies){
				policy.settle(info);
				if(mapper.count(policy.getPolicyNo()) == 0){
					mapper.insert(policy);
					insertCount++;
				}					
			}
			
			session.commit();
			return insertCount;
		} 
		finally{
			if(session != null) session.close();
		}
	}
	
	public static void main(String[] args){
//		Policy policy2 = new Policy("policyNo2","brokerNo1",new BigDecimal("100.1211"),new BigDecimal("19.38"),"company1",
//				"customer1",new Date(),new BigDecimal("26.23"),0.0374,"type1","invoiceNo1",new Date());
//		Policy policy3 = new Policy("policyNo3","brokerNo1",new BigDecimal("100.1251"),new BigDecimal("19.38"),"company1",
//	   			"customer1",new Date(),new BigDecimal("26.23"),0.0374,"type1","invoiceNo1",new Date());
//		Policy policy4 = new Policy("policyNo4","brokerNo1",new BigDecimal("100.12"),new BigDecimal("19.38"),"company1",
//	   			"customer1",new Date(),new BigDecimal("26.23"),0.0374,"type1","invoiceNo1",new Date());
//		Policy policy5 = new Policy("policyNo5","brokerNo1",new BigDecimal("100.12"),new BigDecimal("19.38"),"company1",
//	   			"customer1",new Date(),new BigDecimal("26.23"),0.0374,"type1","invoiceNo1",new Date());
//		List<Policy> policies = new ArrayList();
//		policies.add(policy2);
//		policies.add(policy3);
//		policies.add(policy4);
//		policies.add(policy5);
//		
//		PolicyService policyService = new PolicyService();
//		policyService.addPolicy(policies);
		
		PolicyService policyService = new PolicyService();
		Map<String,Object> queryConditions = new HashMap();
		queryConditions.put("policyNo", "%122%");
		queryConditions.put("brokerNo", null);
		queryConditions.put("insured", null);
		
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			queryConditions.put("inputDateFrom", format.parse("2012-01-01"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		try {
			queryConditions.put("inputDateTo", format.parse("2013-11-26"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		queryConditions.put("remarkScope", RemarkScope.ALL);
		queryConditions.put("settlementScope", SettlementScope.ALL);
		
		
//		System.out.print(policyService.getPolicies(queryConditions).size());
		System.out.print(policyService.settle(policyService.getPolicies(queryConditions), new User("admin","系统管理员","huanya")));
	}
}
