package com.example.productapi.dto;

import com.example.productapi.dto.enums.SalesStatus;

public class SalesConfirmationDTO {

	private String salesId;
	private SalesStatus status;
	private String transactionid;
	
	
	public SalesConfirmationDTO() {
		
	}
	
	public SalesConfirmationDTO(String salesId, SalesStatus status, String transactionid) {
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

	public String getTransactionid() {
		return transactionid;
	}

	public void setTransactionid(String transactionid) {
		this.transactionid = transactionid;
	}
	
	
	
	
	
}
