package com.example.productapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.productapi.dto.SupplierRequest;
import com.example.productapi.dto.SupplierResponse;
import com.example.productapi.service.SupplierService;

@RestController
@RequestMapping("/api/supplier")
public class SupplierController {

	@Autowired
	private SupplierService supplierService;

	@PostMapping
	public SupplierResponse save(@RequestBody SupplierRequest request) {
		return supplierService.save(request);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<SupplierResponse> update(@PathVariable Integer id, @RequestBody SupplierRequest obj) {
		var supplierResponse = supplierService.update(id, obj);
		return ResponseEntity.ok().body(supplierResponse);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<SupplierResponse> findById(@PathVariable Integer id) {
		SupplierResponse obj = supplierService.findById(id);
		return ResponseEntity.ok().body(obj);
	}

	@GetMapping(value = "/name/{name}")
	public ResponseEntity<List<SupplierResponse>> findByDescriptionContainingIgnoreCase(@PathVariable String name) {
		List<SupplierResponse> obj = supplierService.findByNameContainingIgnoreCase(name);
		return ResponseEntity.ok().body(obj);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		supplierService.delete(id);
		return ResponseEntity.noContent().build();
	}

}
