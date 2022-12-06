package com.example.productapi.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.productapi.dto.SalesProductResponse;

@FeignClient(name = "salesClient", contextId = "salesClient", url = "${app-config.services.sales}")
public interface SalesClient {
	
	@GetMapping("products/{productId}")
	SalesProductResponse findSalesByProduct(@PathVariable Integer productId);
	
}
