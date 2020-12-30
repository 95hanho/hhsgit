package com.sejong.hhsweb.model;

import org.apache.ibatis.type.Alias;

@Alias("User")
public class User {

	private String userId;
	private String userPwd;
	private String userEmail;
	private String userConnect;
	// sql에 없는거
	private String connect;

	public User() {
	}

	public User(String userId, String userPwd, String userEmail) {
		super();
		this.userId = userId;
		this.userPwd = userPwd;
		this.userEmail = userEmail;
	}

	public User(String userId, String userPwd, String userEmail, String connect) {
		super();
		this.userId = userId;
		this.userPwd = userPwd;
		this.userEmail = userEmail;
		this.connect = connect;
	}
	
	public User(String userId, String userPwd, String userEmail, String userConnect, String connect) {
		super();
		this.userId = userId;
		this.userPwd = userPwd;
		this.userEmail = userEmail;
		this.userConnect = userConnect;
		this.connect = connect;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserConnect() {
		return userConnect;
	}

	public void setUserConnect(String userConnect) {
		this.userConnect = userConnect;
	}

	public String getConnect() {
		return connect;
	}

	public void setConnect(String connect) {
		this.connect = connect;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", userPwd=" + userPwd + ", userEmail=" + userEmail + ", userConnect="
				+ userConnect + ", connect=" + connect + "]";
	}
}
