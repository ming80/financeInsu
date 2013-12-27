package com.firsthuanya.financeInsu.data;

import java.util.Map;

import com.firsthuanya.financeInsu.domain.User;

public interface UserMapper {
	public User find(String id);
	public int count(Map<String,String> account);
}
