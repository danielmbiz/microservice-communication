package com.example.productapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.productapi.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

	List<Product> findByNameContainingIgnoreCase(String text);

	List<Product> findByCategoryId(Integer id);

	List<Product> findBySupplierId(Integer id);
	
	Boolean existsByCategoryId(Integer id);
	Boolean existsBySupplierId(Integer id);

}
