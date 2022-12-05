package com.example.productapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductRequest {

	private String name;
	private Integer supplierId;
	private Integer categoryId;
	@JsonProperty("quantityAvailable")
	private Integer quantityAvailable;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getSupplierId() {
		return supplierId;
	}
	
	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}
	
	public Integer getCategoryId() {
		return categoryId;
	}
	
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	
	public Integer getQuantityAvailable() {
		return quantityAvailable;
	}
	
	public void setQuantityAvailable(Integer quantityAvailable) {
		this.quantityAvailable = quantityAvailable;
	}
	
	
	
	
	
}
