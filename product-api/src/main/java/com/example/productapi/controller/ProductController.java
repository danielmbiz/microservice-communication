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

import com.example.productapi.dto.ProductCheckStockRequest;
import com.example.productapi.dto.ProductRequest;
import com.example.productapi.dto.ProductResponse;
import com.example.productapi.dto.ProductSalesResponse;
import com.example.productapi.service.ProductService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/product")
public class ProductController {

	@Autowired
	private ProductService productService;

	@PostMapping
	public ProductResponse save(@RequestBody ProductRequest request) {
		return productService.save(request);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<ProductResponse> update(@PathVariable Integer id, @RequestBody ProductRequest obj) {
		var productResponse = productService.update(id, obj);
		return ResponseEntity.ok().body(productResponse);
	}

	@GetMapping
	public ResponseEntity<List<ProductResponse>> findByAll() {
		List<ProductResponse> obj = productService.findAll();
		return ResponseEntity.ok().body(obj);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<ProductResponse> findById(@PathVariable Integer id) {
		ProductResponse obj = productService.findById(id);
		return ResponseEntity.ok().body(obj);
	}

	@GetMapping(value = "/name/{name}")
	public ResponseEntity<List<ProductResponse>> findByDescriptionContainingIgnoreCase(@PathVariable String name) {
		List<ProductResponse> obj = productService.findByNameContainingIgnoreCase(name);
		return ResponseEntity.ok().body(obj);
	}

	@GetMapping(value = "/category/{id}")
	public ResponseEntity<List<ProductResponse>> findByCategoryId(@PathVariable Integer categoryId) {
		List<ProductResponse> obj = productService.findByCategoryId(categoryId);
		return ResponseEntity.ok().body(obj);
	}

	@GetMapping(value = "/supplier/{id}")
	public ResponseEntity<List<ProductResponse>> findBySupplierId(@PathVariable Integer supplierId) {
		List<ProductResponse> obj = productService.findBySupplierId(supplierId);
		return ResponseEntity.ok().body(obj);
	}

	@PostMapping("/check-stock")
	public ResponseEntity<Void> checkProductsStock(@RequestBody ProductCheckStockRequest request) {
		log.info("Requisição POST de novo PEDIDO: ${JSON.stringify(orderData)} | [transationId: ${transactionid}] | [serviceId: ${serviceid}]");
		productService.checkProductsStock(request);
		return ResponseEntity.ok().build();
	}

	@GetMapping(value = "/{id}/sales")
	public ResponseEntity<ProductSalesResponse> findProductSales(@PathVariable Integer id) {
		ProductSalesResponse obj = productService.findProductSales(id);
		return ResponseEntity.ok().body(obj);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		productService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
