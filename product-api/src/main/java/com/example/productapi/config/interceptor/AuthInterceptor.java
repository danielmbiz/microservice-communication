package com.example.productapi.config.interceptor;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.productapi.service.JwtService;
import com.example.productapi.service.exceptions.ValidationException;

import feign.Request.HttpMethod;

public class AuthInterceptor implements HandlerInterceptor {

	private static String AUTHORIZATION = "Authorization";
	private static String TRANSACTIONID = "transactionid";
	
	@Autowired
	private JwtService jwtService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (isOption(request)) {
			return true;
		}
		var authorization = request.getHeader(AUTHORIZATION);
		if (isEmpty(request.getHeader(TRANSACTIONID))) {
			throw new ValidationException("Obrigatório o envio do transactionid");
		}
		jwtService.validateAuthorization(authorization);
		request.setAttribute("serviceid", UUID.randomUUID().toString());
		return true;
	}

	private boolean isOption(HttpServletRequest request) {
		return HttpMethod.OPTIONS.name().equals(request.getMethod());
	}
}
