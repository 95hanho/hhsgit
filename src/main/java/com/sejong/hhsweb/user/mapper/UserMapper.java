package com.sejong.hhsweb.user.mapper;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import com.sejong.hhsweb.model.User;

@Mapper
public interface UserMapper {
	void userInsert(User user);

	User selectUser(String userId);

	ArrayList<User> AllSelectUser(String userId);

	void updateUserConnect(String userId);
}
