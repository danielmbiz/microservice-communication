package com.example.productapi.service;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.productapi.dto.ProductRequest;
import com.example.productapi.dto.ProductResponse;
import com.example.productapi.model.Category;
import com.example.productapi.model.Product;
import com.example.productapi.model.Supplier;
import com.example.productapi.repository.ProductRepository;
import com.example.productapi.service.exceptions.DatabaseException;
import com.example.productapi.service.exceptions.ResourceNotFoundException;
import com.example.productapi.service.exceptions.ValidationException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;

	@Autowired
	private SupplierService supplierService;

	@Autowired
	private CategoryService categoryService;

	public ProductResponse findById(Integer id) {
		if (isEmpty(id)) {
			throw new ValidationException("Informe o Id");
		}
		var product = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado Id: " + id));
		return new ProductResponse(product);
	}

	public List<ProductResponse> findAll() {
		List<ProductResponse> list = repository.findAll().stream().map(x -> new ProductResponse(x))
				.collect(Collectors.toList());
		return list;
	}

	public List<ProductResponse> findByNameContainingIgnoreCase(String name) {
		if (isEmpty(name)) {
			throw new ValidationException("Informe o nome");
		}
		List<ProductResponse> list = repository.findByNameContainingIgnoreCase(name).stream()
				.map(x -> new ProductResponse(x)).collect(Collectors.toList());
		return list;
	}

	public List<ProductResponse> findByCategoryId(Integer categoryId) {
		if (isEmpty(categoryId)) {
			throw new ValidationException("Informe o id da categoria");
		}
		List<ProductResponse> list = repository.findByCategoryId(categoryId).stream().map(x -> new ProductResponse(x))
				.collect(Collectors.toList());
		return list;
	}

	public List<ProductResponse> findBySupplierId(Integer supplierId) {
		if (isEmpty(supplierId)) {
			throw new ValidationException("Informe o Id do fornecedor");
		}
		List<ProductResponse> list = repository.findBySupplierId(supplierId).stream().map(x -> new ProductResponse(x))
				.collect(Collectors.toList());
		return list;
	}

	public ProductResponse save(ProductRequest request) {
		validateProductDataInformed(request);
		validateSupplierAndCategoryInformed(request);
		var category = new Category(categoryService.findById(request.getCategoryId()));
		var supplier = new Supplier(supplierService.findById(request.getSupplierId()));
		var product = new ProductResponse(repository.save(new Product(request, supplier, category)));
		return product;
	}

	public ProductResponse update(Integer id, ProductRequest obj) {
		try {
			findById(id); 
			System.out.println(obj.getCategoryId());
			var category = new Category(categoryService.findById(obj.getCategoryId()));
			var supplier = new Supplier(supplierService.findById(obj.getSupplierId()));
			var product = new Product(obj, supplier, category);
			return new ProductResponse(repository.save(product));
		} catch (RuntimeException e) {
			throw new ResourceNotFoundException(e.getMessage());
		}
	}

	public void delete(Integer id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Produto não encontrado Id: " + id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException(e.getMessage());
		} catch (Exception e) {
			e.getMessage();
		}

	}

	private void validateProductDataInformed(ProductRequest request) {
		if (isEmpty(request.getName())) {
			throw new ValidationException("Nome não informada");
		}
		if (isEmpty(request.getQuantityAvailable())) {
			throw new ValidationException("Quantidade não informada");
		}
		if (request.getQuantityAvailable() <= 0) {
			throw new ValidationException("Quantidade precisa ser maior que zero");
		}
	}

	private void validateSupplierAndCategoryInformed(ProductRequest request) {
		if (isEmpty(request.getCategoryId())) {
			throw new ValidationException("Categoria não informada");
		}
		if (isEmpty(request.getSupplierId())) {
			throw new ValidationException("Fornecedor não informado");
		}
	}

}
