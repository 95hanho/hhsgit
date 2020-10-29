package com.sejong.hhsweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@SpringBootApplication
public class HhswebApplication {

	public static void main(String[] args) {
		SpringApplication.run(HhswebApplication.class, args);
	}
	
}
