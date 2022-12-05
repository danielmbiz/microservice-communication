package com.example.productapi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.beans.BeanUtils;

import com.example.productapi.dto.SupplierRequest;
import com.example.productapi.dto.SupplierResponse;

@Entity
@Table(name = "supplier")
public class Supplier {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id;

	@Column(name = "name", nullable = false)
	private String name;

	public Supplier() {
		
	}
	
	public Supplier(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public Supplier(SupplierResponse response) {
		super();
		this.id = response.getId();
		this.name = response.getName();
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
	
	public static Supplier of(SupplierRequest request) {
		var supplier = new Supplier();
		BeanUtils.copyProperties(request, supplier);
		return supplier;		
	}

}
