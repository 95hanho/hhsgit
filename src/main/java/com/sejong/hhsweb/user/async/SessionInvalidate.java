package com.sejong.hhsweb.user.async;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.socket.WebSocketSession;

import com.sejong.hhsweb.model.User;
import com.sejong.hhsweb.talk.websocket.WebSocketHadler;
import com.sejong.hhsweb.user.service.UserService;

@Service
public class SessionInvalidate {
	
	@Autowired
	private UserService userService;
	
	@Async
	public void sessionTimeOutUpdate(HttpSession session, SessionStatus status) {
		User loginUser = (User)session.getAttribute("loginUser");
		String userId = null;
		if(loginUser != null) {
			userId = loginUser.getUserId();
		}
		
		while(loginUser != null) {
			try {
				Thread.sleep(1000 * 60 * 30); // 30분
//				Thread.sleep(4000); // 4초
//				Thread.sleep(10000); // 10초
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				loginUser = (User) session.getAttribute("loginUser");
			} catch (Exception e) {
				System.out.println(userId + "오류로 세션이 종료됨");
				userService.updateUserConnect(userId);
				status.setComplete();
				List<WebSocketSession> sessions = WebSocketHadler.sessions;
				Map<String, WebSocketSession> userSessionsMap = WebSocketHadler.userSessionsMap;
				userSessionsMap.remove(userId);
				sessions.remove(session);
				return;
			}
		}
		System.out.println(userId + "세션이 종료됨");
	}
}
