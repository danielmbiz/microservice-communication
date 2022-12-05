package com.example.productapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.productapi.model.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {

	List<Supplier> findByNameContainingIgnoreCase(String text);
}
