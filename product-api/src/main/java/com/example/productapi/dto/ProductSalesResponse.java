package com.example.productapi.dto;

import java.util.List;

import com.example.productapi.model.Category;
import com.example.productapi.model.Supplier;

public class ProductSalesResponse {

	private Integer id;
	private String name;
	private Integer quantityAvailable;
	private Supplier supplier;
	private Category category;		
	private List<String> sales;
	
	public ProductSalesResponse() {
		
	}
	
	public ProductSalesResponse(Integer id, String name, Integer quantityAvailable, Supplier supplier,
			Category category, List<String> sales) {
		super();
		this.id = id;
		this.name = name;
		this.quantityAvailable = quantityAvailable;
		this.supplier = supplier;
		this.category = category;
		this.sales = sales;
	}
	
	public ProductSalesResponse(ProductResponse product, List<String> sales) {
		super();
		this.id = product.getId();
		this.name = product.getName();
		this.quantityAvailable = product.getQuantityAvailable();
		this.supplier = product.getSupplier();
		this.category = product.getCategory();
		this.sales = sales;
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

	public Integer getQuantityAvailable() {
		return quantityAvailable;
	}

	public void setQuantityAvailable(Integer quantityAvailable) {
		this.quantityAvailable = quantityAvailable;
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

	public List<String> getSales() {
		return sales;
	}

	public void setSales(List<String> sales) {
		this.sales = sales;
	}
		
}
