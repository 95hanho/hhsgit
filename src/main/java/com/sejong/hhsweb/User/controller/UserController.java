package com.sejong.hhsweb.User.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.sejong.hhsweb.User.service.UserService;
import com.sejong.hhsweb.model.User;

@Controller
@SessionAttributes({"loginUser"})
public class UserController {
	
	static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserService userService;
	
	
	@GetMapping("/")
	public String loginMain() {
		logger.info("loginMain");
		return "user/loginMain";
	}
	
	@PostMapping("userLogin")
	public String userLogin(Model m, @ModelAttribute User user) {
		User OriginUser = userService.selectUser(user.getUserId());
		if(passwordEncoder.matches(user.getUserPwd(), OriginUser.getUserPwd())) {
			m.addAttribute("loginUser", user);
		} else {
			logger.info("login fail!!");
			m.addAttribute("message", "로그인 실패!!");
			return "user/loginMain";
		}
		
		logger.info("userLogin");
		return "chat/chatMain";
	}
	
	@GetMapping("logininfoView")
	public String loginInfoView() {
		logger.info("loginInfo");
		return "user/loginInfo";
	}
	
	@PostMapping("userInsert")
	public String userInsert(Model m, @ModelAttribute User user, HttpSession session) {
		try {
			logger.info("userInsert");
			user.setUserPwd(passwordEncoder.encode(user.getUserPwd()));
			userService.userInsert(user);
			m.addAttribute("loginUser",user);
			m.addAttribute("message", "회원가입 성공!!");
		} catch (Exception e) {
			logger.info("userInsert error");
			m.addAttribute("message", "회원가입 실패!!");
			return "user/loginMain";
		}
		
		return "chat/chatMain";
	}
	
	

}
