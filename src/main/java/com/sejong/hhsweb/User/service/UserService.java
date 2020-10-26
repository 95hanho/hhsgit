package com.sejong.hhsweb.User.service;

import com.sejong.hhsweb.model.User;

public interface UserService {

	void userInsert(User user);

	User selectUser(String userId);
	
}
