package com.example.productapi.config.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.productapi.service.JwtService;

import feign.Request.HttpMethod;

public class AuthInterceptor implements HandlerInterceptor {

	private static String AUTHORIZATION = "Authorization";
	@Autowired
	private JwtService jwtService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (isOption(request)) {
			return true;
		}
		var authorization = request.getHeader(AUTHORIZATION);
		jwtService.validateAuthorization(authorization);		
		return true;
	}

	private boolean isOption(HttpServletRequest request) {
		return HttpMethod.OPTIONS.name().equals(request.getMethod());
	}
}
