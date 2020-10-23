package com.sejong.hhsweb.User.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
	
	HttpSession session;
	
	@GetMapping("/")
	public String loginMain() {
		return "User/loginMain";
	}
	

}
