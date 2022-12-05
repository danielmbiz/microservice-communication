package com.example.productapi.service;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.productapi.dto.SupplierRequest;
import com.example.productapi.dto.SupplierResponse;
import com.example.productapi.model.Supplier;
import com.example.productapi.repository.SupplierRepository;
import com.example.productapi.service.exceptions.DatabaseException;
import com.example.productapi.service.exceptions.ResourceNotFoundException;
import com.example.productapi.service.exceptions.ValidationException;

@Service
public class SupplierService {

	@Autowired
	private SupplierRepository repository;

	public SupplierResponse findById(Integer id) {
		validateIdInformed(id);
		var supplier = repository.findById(id)
				.orElseThrow(() -> new DatabaseException("Fornecedor não encontrado Id: "+id));
		return new SupplierResponse(supplier);
	}

	public List<SupplierResponse> findAll() {
		List<SupplierResponse> list = repository.findAll().stream().map(x -> new SupplierResponse(x))
				.collect(Collectors.toList());
		return list;
	}

	public List<SupplierResponse> findByNameContainingIgnoreCase(String name) {
		if (isEmpty(name)) {
			throw new ValidationException("Informe a descrição");
		}
		List<SupplierResponse> list = repository.findByNameContainingIgnoreCase(name).stream()
				.map(x -> new SupplierResponse(x)).collect(Collectors.toList());
		return list;
	}

	public SupplierResponse save(SupplierRequest request) {
		validateSupplierNameInformed(request);
		var supplier = repository.save(Supplier.of(request));
		return SupplierResponse.of(supplier);
	}

	public SupplierResponse update(Integer id, SupplierRequest obj) {
		try {
			findById(id);
			var supplier = Supplier.of(obj);
			supplier.setId(id);
			return SupplierResponse.of(repository.save(supplier));
		} catch (RuntimeException e) {
			throw new ResourceNotFoundException(e.getMessage());
		}
	}

	private void validateSupplierNameInformed(SupplierRequest request) {
		if (isEmpty(request.getName())) {
			throw new ValidationException("Categoria não informada");
		}
	}

	public void delete(Integer id) {
		try {
			validateIdInformed(id);
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Fornecedor não encontrado Id: "+id);
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

}
