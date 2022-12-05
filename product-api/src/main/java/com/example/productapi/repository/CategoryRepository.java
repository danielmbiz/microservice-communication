package com.example.productapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.productapi.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
	
	List<Category> findByDescriptionContainingIgnoreCase(String text);
	
}
