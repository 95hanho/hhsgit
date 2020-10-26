package com.sejong.hhsweb.User.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.sejong.hhsweb.model.User;

@Mapper
public interface UserMapper {
	void userInsert(User user);

	User selectUser(String userId);
}
