package com.example.productapi.config.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.productapi.service.exceptions.ValidationException;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class FeignClienteAuthInterceptor implements RequestInterceptor {

	private static final String AUTHORIZATION = "Authorization";

	@Override
	public void apply(RequestTemplate template) {
		var currentRequest = getCurrentRequest();
		template.header(AUTHORIZATION, currentRequest.getHeader(AUTHORIZATION));

	}

	private HttpServletRequest getCurrentRequest() {
		try {
			return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ValidationException("A requisição não pode ser processada");
		}
	}

}
