package com.sejong.hhsweb.user.service;

import com.sejong.hhsweb.model.User;

public interface UserService {

	void userInsert(User user);

	User selectUser(String userId);
	
}
