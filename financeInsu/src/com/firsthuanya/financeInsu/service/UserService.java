package com.firsthuanya.financeInsu.service;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.firsthuanya.financeInsu.data.UserMapper;
import com.firsthuanya.financeInsu.domain.User;
import com.firsthuanya.financeInsu.exception.UserServiceException;

public class UserService {
	private final static String RESOURCES = "com/firsthuanya/financeInsu/data/Configuration.xml";	

	private SqlSession openSession() throws UserServiceException{
		Reader reader;
		try {
			reader = Resources.getResourceAsReader(RESOURCES);
		} catch (IOException e) {
			throw new UserServiceException("open session failed!",e);
		}
		SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();	
		return builder.build(reader).openSession();
	}
	
	public User getUser(String id) throws UserServiceException{
		if(id == null || id.trim().equals(""))
			throw new IllegalArgumentException("id is null!");	
		SqlSession session = openSession();
		try{
			UserMapper mapper = session.getMapper(UserMapper.class);
			return mapper.find(id);			
		}
		finally{
			if(session != null) session.close();
		}		
	}
	
	public boolean hasSuchUser(String id,String pw){
		if(id == null || id.trim().equals(""))
			throw new IllegalArgumentException("id is null!");
		if(pw == null || pw.trim().equals(""))
			throw new IllegalArgumentException("password is null!");
		
		SqlSession session = openSession();
		
		try{
			UserMapper mapper = session.getMapper(UserMapper.class);
			Map<String, String> account = new HashMap();
			account.put("id", id);
			account.put("password", pw);
			return (mapper.count(account) == 1);
		}
		finally{
			if(session != null) session.close();
		}		
	}

	public static void main(String[] args){
		UserService service = new UserService();
		User user = service.getUser("admin");
//		System.out.print(service.hasSuchUser("admin", "huanya"));
		System.out.print(user.getName());
	}
}
