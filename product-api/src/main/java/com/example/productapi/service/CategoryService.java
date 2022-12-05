package com.example.productapi.service;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.productapi.dto.CategoryRequest;
import com.example.productapi.dto.CategoryResponse;
import com.example.productapi.model.Category;
import com.example.productapi.repository.CategoryRepository;
import com.example.productapi.service.exceptions.DatabaseException;
import com.example.productapi.service.exceptions.ResourceNotFoundException;
import com.example.productapi.service.exceptions.ValidationException;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;

	public CategoryResponse findById(Integer id) {
		validateIdInformed(id);
		var category = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada Id: " + id));
		return new CategoryResponse(category);

	}

	public List<CategoryResponse> findAll() {
		List<CategoryResponse> list = repository.findAll().stream().map(x -> new CategoryResponse(x))
				.collect(Collectors.toList());
		return list;
	}

	public List<CategoryResponse> findByDescriptionContainingIgnoreCase(String description) {
		validateDescriptionInformed(description);
		List<CategoryResponse> list = repository.findByDescriptionContainingIgnoreCase(description).stream()
				.map(x -> new CategoryResponse(x)).collect(Collectors.toList());
		return list;
	}

	public CategoryResponse save(CategoryRequest request) {
		validateCategoryNotInformed(request);
		var category = repository.save(Category.of(request));
		return CategoryResponse.of(category);
	}

	public CategoryResponse update(Integer id, CategoryRequest obj) {
		try {
			findById(id);
			var category = Category.of(obj);
			category.setId(id);
			return CategoryResponse.of(repository.save(category));
		} catch (RuntimeException e) {
			throw new ResourceNotFoundException(e.getMessage());
		}
	}

	public void delete(Integer id) {
		try {
			validateIdInformed(id);
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Categoria não encontrada Id: " + id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException(e.getMessage());
		} catch (Exception e) {
			e.getMessage();
		}
	}

	private void validateIdInformed(Integer id) {
		if (isEmpty(id)) {
			throw new ValidationException("Informe o Id");
		}
	}

	private void validateDescriptionInformed(String description) {
		if (isEmpty(description)) {
			throw new ValidationException("Informe a descrição");
		}
	}

	private void validateCategoryNotInformed(CategoryRequest request) {
		if (isEmpty(request.getDescription())) {
			throw new ValidationException("Categoria não informada");
		}
	}

}
