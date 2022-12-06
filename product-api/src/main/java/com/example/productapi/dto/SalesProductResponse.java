package com.example.productapi.dto;

import java.util.List;

public class SalesProductResponse {
	
	private List<String> salesId;

	public SalesProductResponse() {
		
	}
	
	public SalesProductResponse(List<String> salesId) {
		super();
		this.salesId = salesId;
	}

	public List<String> getSalesId() {
		return salesId;
	}

	public void setSalesId(List<String> salesId) {
		this.salesId = salesId;
	}
	
	

}
