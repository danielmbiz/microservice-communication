package com.example.productapi.dto;

import org.springframework.beans.BeanUtils;

import com.example.productapi.model.Category;

public class CategoryResponse {
	
	private Integer id;
	private String description;	
	
	public CategoryResponse() {
		
	}
	
	public CategoryResponse(Category category) {
		super();
		this.id = category.getId();
		this.description = category.getDescription();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static CategoryResponse of(Category category) {
		var response = new CategoryResponse();
		BeanUtils.copyProperties(category, response);
		return response;		
	}
}
