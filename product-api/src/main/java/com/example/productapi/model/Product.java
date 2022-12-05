package com.example.productapi.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.example.productapi.dto.ProductRequest;
import com.example.productapi.dto.ProductResponse;

@Entity
@Table(name = "product")
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id;
	
	@Column(nullable = false)
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "FK_SUPPLIER", nullable = false)
	private Supplier supplier;
	
	@ManyToOne
	@JoinColumn(name = "FK_CATEGORY", nullable = false)
	private Category category;
			
	@Column(nullable = false)
	private Integer quantityAvailable;
	
	@Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

	public Product() {
		
	}
	
	public Product(Integer id, String name, Supplier supplier, Category category, Integer quantityAvailable) {
		super();
		this.id = id;
		this.name = name;
		this.supplier = supplier;
		this.category = category;
		this.quantityAvailable = quantityAvailable;
	}
	
	public Product(ProductResponse response) {
		super();
		this.id = response.getId();
		this.name = response.getName();
		this.quantityAvailable = response.getQuantityAvailable();
		this.supplier = response.getSupplier();
		this.category = response.getCategory();
	}
	
	public Product(ProductRequest request, Supplier supplier, Category category) {
		super();
		this.name = request.getName();
		this.quantityAvailable = request.getQuantityAvailable();
		this.supplier = supplier;
		this.category = category;
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
