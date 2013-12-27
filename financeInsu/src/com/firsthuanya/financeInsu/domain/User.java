package com.firsthuanya.financeInsu.domain;

public class User {
	private String id;
	private String name;
	private String password;
	
	private User(){		
	}
	
	public User(String id,String name,String password){
		if(id == null || id.trim().equals(""))
			throw new IllegalArgumentException("id is null!");
		if(name == null || name.trim().equals(""))
			throw new IllegalArgumentException("name is null!");
		if(password == null || password.trim().equals(""))
			throw new IllegalArgumentException("password is null!");
		
		this.id = id;
		this.name = name;
		this.password = password;		
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPassword() {
		return password;
	}
	
	
}
