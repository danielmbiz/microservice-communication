package com.example.productapi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.beans.BeanUtils;

import com.example.productapi.dto.CategoryRequest;
import com.example.productapi.dto.CategoryResponse;

@Entity
@Table(name = "category")
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id;

	@Column(name = "description", nullable = false)
	private String description;

	public Category() {

	}

	public Category(Integer id, String description) {
		super();
		this.id = id;
		this.description = description;
	}

	public Category(CategoryResponse response) {
		super();
		this.id = response.getId();
		this.description = response.getDescription();
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

	public static Category of(CategoryRequest request) {
		var category = new Category();
		BeanUtils.copyProperties(request, category);
		return category;
	}

}
