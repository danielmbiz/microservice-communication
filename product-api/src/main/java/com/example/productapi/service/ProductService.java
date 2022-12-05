package com.example.productapi.service;

import static org.springframework.util.ObjectUtils.isEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.productapi.config.exception.ValidationException;
import com.example.productapi.dto.ProductRequest;
import com.example.productapi.dto.ProductResponse;
import com.example.productapi.model.Category;
import com.example.productapi.model.Product;
import com.example.productapi.repository.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;

	@Autowired
	private SupplierService supplierService;

	@Autowired
	private CategoryService categoryService;

	public Product findById(Integer id) {
		return repository.findById(id).orElseThrow(() -> new ValidationException("Produto não encontrado ID: " + id));
	}

	public ProductResponse save(ProductRequest request) {
		validateProductDataInformed(request);		
		validateSupplierAndCategoryInformed(request);		
		var category = new Category(categoryService.findById(request.getCategoryId()));
		var supplier = supplierService.findById(request.getSupplierId());		
		var product = new ProductResponse(repository.save(new Product(request, supplier, category)));				
		return product;
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
