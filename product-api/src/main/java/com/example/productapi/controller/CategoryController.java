package com.example.productapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.productapi.dto.CategoryRequest;
import com.example.productapi.dto.CategoryResponse;
import com.example.productapi.service.CategoryService;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
	
	@Autowired
	private CategoryService categoryService;
	
	@PostMapping
	public CategoryResponse save(@RequestBody CategoryRequest request) {
		return categoryService.save(request);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<CategoryResponse> findById(@PathVariable Integer id) {
		CategoryResponse obj = categoryService.findById(id);
		return ResponseEntity.ok().body(obj);
	}
	
	@GetMapping(value = "/description/{description}")
	public ResponseEntity<List<CategoryResponse>> findByDescriptionContainingIgnoreCase(@PathVariable String description) {
		List<CategoryResponse> obj = categoryService.findByDescriptionContainingIgnoreCase(description);
		return ResponseEntity.ok().body(obj);
	}

}
