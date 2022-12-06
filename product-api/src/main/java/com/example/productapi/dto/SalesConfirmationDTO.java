package com.example.productapi.dto;

import com.example.productapi.dto.enums.SalesStatus;

public class SalesConfirmationDTO {

	private String salesId;
	private SalesStatus status;
	
	
	public SalesConfirmationDTO() {
		
	}
	
	public SalesConfirmationDTO(String salesId, SalesStatus status) {
		super();
		this.salesId = salesId;
		this.status = status;
	}

	public String getSalesId() {
		return salesId;
	}

	public void setSalesId(String salesId) {
		this.salesId = salesId;
	}

	public SalesStatus getStatus() {
		return status;
	}

	public void setStatus(SalesStatus status) {
		this.status = status;
	}
	
	
	
	
	
}
