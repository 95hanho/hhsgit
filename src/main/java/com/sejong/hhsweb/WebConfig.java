package com.sejong.hhsweb;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new CertificationInterceptor())
		.excludePathPatterns("/", "/userLogin", "/talk/**", "/uploadfiles/**", "/user/**", "/websocket/**");
//		.excludePathPatterns("/talk/**", "/uploadfiles/**", "/user/**", "/websocket/**");
	}
}
