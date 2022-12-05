package com.example.productapi.service;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.productapi.config.exception.ValidationException;
import com.example.productapi.dto.CategoryRequest;
import com.example.productapi.dto.CategoryResponse;
import com.example.productapi.model.Category;
import com.example.productapi.repository.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;

	public CategoryResponse findById(Integer id) {
		if (isEmpty(id)) {
			throw new ValidationException("Informe o Id");
		}
		var category = repository.findById(id)
				.orElseThrow(() -> new ValidationException("Categoria não encontrada ID: " + id));
		return new CategoryResponse(category);
	}

	public List<CategoryResponse> findAll() {
		List<CategoryResponse> list = repository.findAll().stream().map(x -> new CategoryResponse(x))
				.collect(Collectors.toList());
		return list;
	}

	public List<CategoryResponse> findByDescriptionContainingIgnoreCase(String description) {
		if (isEmpty(description)) {
			throw new ValidationException("Informe a descrição");
		}
		List<CategoryResponse> list = repository.findByDescriptionContainingIgnoreCase(description).stream()
				.map(x -> new CategoryResponse(x)).collect(Collectors.toList());
		return list;
	}

	public CategoryResponse save(CategoryRequest request) {
		validateCategoryNameInformed(request);
		var category = repository.save(Category.of(request));
		return CategoryResponse.of(category);
	}

	private void validateCategoryNameInformed(CategoryRequest request) {
		if (isEmpty(request.getDescription())) {
			throw new ValidationException("Categoria não informada");
		}
	}

}
