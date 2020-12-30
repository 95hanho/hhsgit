package com.sejong.hhsweb.user.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.sejong.hhsweb.model.User;
import com.sejong.hhsweb.user.async.SessionInvalidate;
import com.sejong.hhsweb.user.service.UserService;

@Controller
@SessionAttributes({ "loginUser" })
public class UserController {

	static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserService userService;

	@Autowired
	private SessionInvalidate si;

	// 첫 화면 및 로그아웃 뷰
	@GetMapping("/")
	public String loginMain(Model m, @RequestParam(value = "ms", required = false) String ms,
			@RequestParam(value = "webmessage", required = false) String webmessage,
			@RequestParam(value = "message", required = false) String message) {
		logger.info("loginMain");

		// ms="loginout:"가 있으면 섹션만료로 메인화면로딩 시 알람뜸
		if (ms != null) {
			m.addAttribute("ms", ms);
		}

		// 웹메시지 있으면 메인 실행 시 웹소켓서버에 웹메시지보냄
		if (webmessage != null) {
			m.addAttribute("webmessage", webmessage);
		}

		if(message != null) {
			m.addAttribute("message", message);
		}
		
		return "user/loginMain";
	}

	// 로그인 할때
	@PostMapping("userLogin")
	public String userLogin(Model m, @ModelAttribute User user, HttpSession session) {

		User OriginUser = userService.selectUser(user.getUserId());
		// ID가 존재하고 비밀번호와 일치하면 로그인 성공
		if (OriginUser != null && passwordEncoder.matches(user.getUserPwd(), OriginUser.getUserPwd())) {
			m.addAttribute("loginUser", OriginUser);
			return "redirect:loginComplete";
		} else {
			logger.info("login fail!!");
			String message = null;
			try {
				message = URLEncoder.encode("로그인 실패!!", "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return "redirect:/?message="+message;
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
		// 로그인 시 섹션이 유지되어 있는지 확인시켜주고 아닐 시 로그인정보를 업데이트해주는 스레드를 실행
		si.sessionTimeOutUpdate(session);
		logger.info(userId + " : login success");
		// webmessage에 loginout을 보내서 접속회원 접속정보 초기화
		m.addAttribute("webmessage", "loginout:");
		
		return "talk/talkMain";
	}

	// 회원가입 뷰
	@GetMapping("logininfoView")
	public String loginInfoView() {
		logger.info("loginInfo");
		return "user/loginInfo";
	}

	// 로그아웃 로직: 섹션종료 및 로그아웃기록
	@GetMapping("logoutView")
	public String logout(SessionStatus status, HttpSession session) {
		logger.info("logout");
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser != null) {
			String userId = loginUser.getUserId();
			userService.updateUserConnect(userId); // 로그인
		}
		status.setComplete(); // 섹션 종료

		// webmessage에 loginout을 보내서 접속회원 접속정보 초기화
		return "redirect:/?webmessage=loginout:";
	}

	// 회원가입 완료시
	@PostMapping(value = "userInsert", produces = "text/plain")
	public String userInsert(Model m, @ModelAttribute User user) {
		try {
			logger.info("userInsert");
			user.setUserPwd(passwordEncoder.encode(user.getUserPwd()));
			userService.userInsert(user);
			m.addAttribute("loginUser", user);
		} catch (Exception e) {
			logger.info("userInsert error");
			String message = null;
			try {
				message = URLEncoder.encode("회원가입 실패!!", "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			return "redirect:/?message="+message;
		}
		return "redirect:loginComplete";
	}

}
