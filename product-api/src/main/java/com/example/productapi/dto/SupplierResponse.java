package com.example.productapi.dto;

import org.springframework.beans.BeanUtils;

import com.example.productapi.model.Supplier;

public class SupplierResponse {

	private Integer id;
	private String name;

	public SupplierResponse() {

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

	public static SupplierResponse of(Supplier supplier) {
		var response = new SupplierResponse();
		BeanUtils.copyProperties(supplier, response);
		return response;
	}
}
