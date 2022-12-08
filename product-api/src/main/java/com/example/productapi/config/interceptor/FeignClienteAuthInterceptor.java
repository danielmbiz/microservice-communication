package com.example.productapi.config.interceptor;

import org.springframework.stereotype.Component;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import static com.example.productapi.config.RequestUtil.getCurrentRequest;; 

@Component
public class FeignClienteAuthInterceptor implements RequestInterceptor {

	private static final String AUTHORIZATION = "Authorization";
	private static String TRANSACTIONID = "transactionid";

	@Override
	public void apply(RequestTemplate template) {
		var currentRequest = getCurrentRequest();
		template.header(AUTHORIZATION, currentRequest.getHeader(AUTHORIZATION))
		        .header(TRANSACTIONID, currentRequest.getHeader(TRANSACTIONID));

	}

	

}
