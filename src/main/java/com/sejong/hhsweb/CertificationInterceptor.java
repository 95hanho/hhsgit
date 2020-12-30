package com.sejong.hhsweb;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.sejong.hhsweb.model.User;

@Component
public class CertificationInterceptor extends HandlerInterceptorAdapter {
	
	// 서버의 요청 시 컨트롤러로 가기전에 실행시켜줌
	// 섹션에 로그인한 유저정보가 있다면 섹션시간을 연장시켜주고 아니라면 메인화면에 섹션만료 알람(ms)를 보냄
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession();
		User loginVo = (User) session.getAttribute("loginUser");

		if (loginVo != null) {
			session.setMaxInactiveInterval(60 * 30); // 30분(60 * 30)
//			session.setMaxInactiveInterval(5);
			return true;
		} else {
			response.sendRedirect("/?ms=loginout:");
			return false;
		}
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		super.afterCompletion(request, response, handler, ex);
	}

}
