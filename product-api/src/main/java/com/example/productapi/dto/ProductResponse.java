package com.example.productapi.dto;

import com.example.productapi.model.Category;
import com.example.productapi.model.Product;
import com.example.productapi.model.Supplier;

public class ProductResponse {

	private Integer id;
	private String name;
	private Integer quantityAvailable;
	private Supplier supplier;
	private Category category;		
	
	public ProductResponse() {
		
	}
	public ProductResponse(Integer id, String name, Integer quantityAvailable, Supplier supplier, Category category) {
		super();
		this.id = id;
		this.name = name;
		this.quantityAvailable = quantityAvailable;
		this.supplier = supplier;
		this.category = category;
	}

	public ProductResponse(Product product) {
		super();
		this.id = product.getId();
		this.name = product.getName();
		this.quantityAvailable = product.getQuantityAvailable();
		this.supplier = product.getSupplier();
		this.category = product.getCategory();
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Integer getQuantityAvailable() {
		return quantityAvailable;
	}

	public void setQuantityAvailable(Integer quantityAvailable) {
		this.quantityAvailable = quantityAvailable;
	}
	
	
	
}
