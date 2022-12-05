package com.example.productapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.productapi.model.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {

}
