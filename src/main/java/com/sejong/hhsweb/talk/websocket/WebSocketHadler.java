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
	List<WebSocketSession> sessions = new ArrayList<WebSocketSession>();
	// 1대1
	Map<String, WebSocketSession> userSessionsMap = new HashMap<String, WebSocketSession>();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		Thread.sleep(500); // 아다리 맞추기
		sessions.add(session); // 전체사용 List에 담기
		String userId = getUserId(session);
		logger.info(userId + "웹소켓 연결");
		userSessionsMap.put(userId, session); // 하나씩 사용 리스트에 담기
		logger.info("in" + userSessionsMap.keySet().toString());
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String userId = getUserId(session);
		String receiveM = message.getPayload();
		logger.debug("received message : " + receiveM);
		Thread.sleep(500); // 아다리 맞추기
		String[] divideM = receiveM.split(":");
		
		if(divideM[0].equals("talkmake")) {
			String[] memList = divideM[1].split(",");
			for(int i=0;i<memList.length;i++) {
				if(!memList[i].equals(userId)) {
					WebSocketSession reMemSession = userSessionsMap.get(memList[i]);
					if(reMemSession != null) {
						reMemSession.sendMessage(new TextMessage("reTalkInfo"));
						logger.info("reTalkInfo message to /////" + memList[i]);
					}
				}
			}
		}
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		String userId = getUserId(session);
		userSessionsMap.remove(userId);
		sessions.remove(session);
		logger.info(userId + "-->웹소켓 연결 해제");
		logger.info("나가고 in" + userSessionsMap.keySet().toString());
	}
	
	private String getUserId(WebSocketSession session) {
		Map<String, Object> httpSession = session.getAttributes();
		User loginUser = (User)httpSession.get("loginUser");	
		
		return loginUser.getUserId();
	}
	
}
