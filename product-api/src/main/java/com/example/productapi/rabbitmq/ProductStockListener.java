package com.example.productapi.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.productapi.dto.ProductStockDTO;
import com.example.productapi.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ProductStockListener {

	@Autowired
	private ProductService productService;

	@RabbitListener(queues = "${app-config.rabbit.queue.product-stock}")
	public void recieverProductStockMessage(ProductStockDTO product) {
		
		log.info(null, product);
		productService.updateProductStock(product);
	}

}
