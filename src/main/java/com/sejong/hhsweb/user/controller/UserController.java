package com.sejong.hhsweb.user.controller;

import java.util.ArrayList;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.sejong.hhsweb.model.User;
import com.sejong.hhsweb.user.service.UserService;

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
		// 로그인이 아닐 때 talkMain갈때 ===>>  (redirect 방식으로 다시 talkMain으로 가면 웹소켓 초기화가 안되므로 사용안함)
//		if(user.getUserId() == null) {
//			String userId = ((User)session.getAttribute("loginUser")).getUserId();
//			ArrayList<User> allUserList = userService.AllSelectUser(userId);
//			m.addAttribute("allUserList", allUserList);
//			return "talk/talkMain";
//		}
		
		// 로그인 할때
		User OriginUser = userService.selectUser(user.getUserId());
		if(OriginUser != null && passwordEncoder.matches(user.getUserPwd(), OriginUser.getUserPwd())) {
			m.addAttribute("loginUser", user);
			return "redirect:loginComplete";
		} else {
			logger.info("login fail!!");
			m.addAttribute("message", "로그인 실패!!");
			return "user/loginMain";
		}
	}
	
	// Post방식은 Get방식으로 다시 뷰로 가줘야 새로고침이나 뒤로가기 시 재호출가능
	@GetMapping("loginComplete")
	public String loginComplete(Model m, HttpSession session) {
		String userId;
		try {
			userId = ((User) session.getAttribute("loginUser")).getUserId();
		} catch (Exception e) {
			return "redirect:/";
		}
		ArrayList<User> allUserList = userService.AllSelectUser(userId);
		m.addAttribute("allUserList", allUserList);
		logger.info(userId + "login success");
		return "talk/talkMain";
	}
	
	@GetMapping("logininfoView")
	public String loginInfoView() {
		logger.info("loginInfo");
		return "user/loginInfo";
	}
	
	@GetMapping("logoutView")
	public String logout(SessionStatus status) {
		logger.info("logout");
		status.setComplete();
		
		return "redirect:/";
	}
	
	@PostMapping(value = "userInsert", produces="text/plain")
	public String userInsert(Model m, @ModelAttribute User user) {
		try {
			logger.info("userInsert");
			user.setUserPwd(passwordEncoder.encode(user.getUserPwd()));
			userService.userInsert(user);
			String userId = user.getUserId();
			ArrayList<User> allUserList = userService.AllSelectUser(userId);
			m.addAttribute("allUserList", allUserList);
			m.addAttribute("loginUser",user);
			m.addAttribute("message", "회원가입 성공!!");
		} catch (Exception e) {
			logger.info("userInsert error");
			m.addAttribute("message", "회원가입 실패!!");
			return "user/loginMain";
		}
		return "redirect:loginComplete";
	}
	
	

}
