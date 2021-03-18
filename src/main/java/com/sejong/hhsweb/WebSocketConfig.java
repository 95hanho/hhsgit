package com.sejong.hhsweb;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import com.sejong.hhsweb.talk.websocket.WebSocketHadler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer{
	private final WebSocketHadler webSocketHandler = new WebSocketHadler();
	private final HttpSessionHandshakeInterceptor interceptors = new HttpSessionHandshakeInterceptor();
	
	
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(webSocketHandler, "/websocket")
				.addInterceptors(interceptors)
				.setAllowedOrigins("*"); 
	}

}
