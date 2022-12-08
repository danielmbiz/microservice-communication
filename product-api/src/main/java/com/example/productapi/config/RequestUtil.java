package com.example.productapi.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.productapi.service.exceptions.ValidationException;

public class RequestUtil {
	
	public static HttpServletRequest getCurrentRequest() {
		try {
			return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ValidationException("A requisição não pode ser processada");
		}
	}

}
