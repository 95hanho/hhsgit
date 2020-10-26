package com.sejong.hhsweb.User.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sejong.hhsweb.User.mapper.UserMapper;
import com.sejong.hhsweb.model.User;

@Repository
public class UserDAO {

	@Autowired
	private UserMapper userMapper;
	
	public void userInsert(User user) {
		userMapper.userInsert(user);
	}

	public User selectUser(String userId) {
		return userMapper.selectUser(userId);
	}

}
