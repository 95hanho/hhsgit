package com.sejong.hhsweb.user.service;

import java.util.ArrayList;

import com.sejong.hhsweb.model.User;

public interface UserService {

	void userInsert(User user);

	User selectUser(String userId);

	ArrayList<User> AllSelectUser(String userId);

	void updateUserConnect(String userId);
	
}
