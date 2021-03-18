package com.sejong.hhsweb.talk.websocket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.sejong.hhsweb.model.User;

@Component
public class WebSocketHadler extends TextWebSocketHandler {

	static final Logger logger = LoggerFactory.getLogger(WebSocketHadler.class);
	
	// 로그인 한 전체
	public static List<WebSocketSession> sessions = new ArrayList<WebSocketSession>();
	// 1대1
	public static Map<String, WebSocketSession> userSessionsMap = new HashMap<String, WebSocketSession>();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		Thread.sleep(250); // 아다리 맞추기
		String userId = getUserId(session);
		if(userId != null) {
			sessions.add(session); // 전체사용 List에 담기
			logger.info(userId + "웹소켓 연결");
			userSessionsMap.put(userId, session); // 하나씩 사용 리스트에 담기
			logger.info("in" + userSessionsMap.keySet());
			session.sendMessage(new TextMessage("userList:" + userSessionsMap.keySet()));
		} else {
			logger.info("no section 웹소켓 연결");
		}
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String userId = getUserId(session);
		String receiveM = message.getPayload(); // 메시지 가져오기
		logger.debug("received message : " + receiveM);
		Thread.sleep(250); // 아다리 맞추기
		String[] divideM = receiveM.split(":");
		
		// 어떤 회원이 로그인, 로그아웃 시 접속해 있는 다른 회원의 접속 기록 목록을 초기화함
		if(divideM[0].equals("loginout")) {
			for(WebSocketSession hs : sessions) {
				if(session != hs) {
					hs.sendMessage(new TextMessage("userList:" + userSessionsMap.keySet()));
				}
			}
		}
		
		if(userId != null) {
			// 톡생성 메시지, 로그인 안된 소켓엔 가면 안되니 userId 있는 곳만
			if(divideM[0].equals("talkmake")) {
				String[] memList = divideM[1].split(",");
				for(int i=0;i<memList.length;i++) {
					if(!memList[i].equals(userId)) {
						WebSocketSession reMemSession = userSessionsMap.get(memList[i]);
						
						if(reMemSession != null) {
							reMemSession.sendMessage(new TextMessage("reTalkInfo"));
						}
					}
				}
			}
		} else {
			
		}
		
		
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		// 웹소켓이 닫힐 때 접속한 유저의 아이디와 섹션을 제거함
		String userId = getUserId(session);
		if(userId != null) {
			userSessionsMap.remove(userId);
			sessions.remove(session);
		}
		
	}
	
	// session을 가지고 HttpSession의 회원아이디를 가져오는 메소드
	private String getUserId(WebSocketSession session) {
		Map<String, Object> httpSession = session.getAttributes();
		User loginUser = (User)httpSession.get("loginUser");
		
		if(loginUser != null) {
			return loginUser.getUserId();
		} else {
			return null;
		}
		
	}
	
}
