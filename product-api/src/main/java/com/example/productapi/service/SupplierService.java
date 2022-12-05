package com.example.productapi.service;

import static org.springframework.util.ObjectUtils.isEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.productapi.config.exception.ValidationException;
import com.example.productapi.dto.SupplierRequest;
import com.example.productapi.dto.SupplierResponse;
import com.example.productapi.model.Supplier;
import com.example.productapi.repository.SupplierRepository;



@Service
public class SupplierService {

	@Autowired
	private SupplierRepository repository;
	
	public Supplier findById(Integer id) {
		return repository.findById(id).orElseThrow(() -> new ValidationException("Fornecedor não encontrado ID: " + id));
	}
	
	public SupplierResponse save(SupplierRequest request) {
		validateSupplierNameInformed(request);
		var supplier = repository.save(Supplier.of(request));
		return SupplierResponse.of(supplier);
	}
	
	private void validateSupplierNameInformed(SupplierRequest request) {
		if (isEmpty(request.getName())) {
			throw new ValidationException("Categoria não informada");
		}
	}
	
}
