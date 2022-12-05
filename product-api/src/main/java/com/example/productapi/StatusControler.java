package com.example.productapi;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/status")
public class StatusControler {
	
	@GetMapping
	public ResponseEntity<HashMap<String, Object>> getStatus() {
		var response = new HashMap<String, Object>();
		response.put("service", "productor-api");
		response.put("status", HttpStatus.OK.value());
		return ResponseEntity.ok(response);
	}

}
