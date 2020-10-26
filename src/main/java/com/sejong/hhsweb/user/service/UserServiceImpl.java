package com.sejong.hhsweb.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sejong.hhsweb.model.User;
import com.sejong.hhsweb.user.dao.UserDAO;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDAO userDAO;
	
	@Override
	public void userInsert(User user) {
		userDAO.userInsert(user);
	}

	@Override
	public User selectUser(String userId) {
		return userDAO.selectUser(userId);
	}
	
}
