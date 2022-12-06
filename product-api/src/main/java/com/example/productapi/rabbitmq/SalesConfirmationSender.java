package com.example.productapi.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.productapi.dto.SalesConfirmationDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SalesConfirmationSender {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Value("${app-config.rabbit.exchange.product}")
	private String productTopicExchange;

	@Value("${app-config.rabbit.routinKey.sales-confirmation}")
	private String salesConfirmationKey;

	public void sendSalesConfirmationMessages(SalesConfirmationDTO message) {
		try {
			log.info("Enviando mensagem: {}", new ObjectMapper().writeValueAsString(message));
			rabbitTemplate.convertAndSend(productTopicExchange, salesConfirmationKey, message);
			log.info("Mensagem enviada com sucesso!");
		} catch (Exception e) {
			log.info("Erro ao enviar mensagem " + e.getMessage());
		}
	}

}
