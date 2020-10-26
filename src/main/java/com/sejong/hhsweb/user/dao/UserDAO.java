package com.sejong.hhsweb.user.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sejong.hhsweb.model.User;
import com.sejong.hhsweb.user.mapper.UserMapper;

@Repository("userDAO")
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
