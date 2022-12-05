package com.example.productapi.dto;

import java.util.List;

public class ProductStockDTO {
	
	private String salesId;
	private List<ProductQuantityDTO> products;
	public ProductStockDTO() {
		
	}
	
	public ProductStockDTO(String salesId, List<ProductQuantityDTO> products) {
		super();
		this.salesId = salesId;
		this.products = products;
	}

	public String getSalesId() {
		return salesId;
	}

	public void setSalesId(String salesId) {
		this.salesId = salesId;
	}

	public List<ProductQuantityDTO> getProducts() {
		return products;
	}

	public void setProducts(List<ProductQuantityDTO> products) {
		this.products = products;
	}
	
	
	
}
