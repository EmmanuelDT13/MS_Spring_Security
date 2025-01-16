package com.emmanuel.api.springsecurity.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emmanuel.api.springsecurity.persistence.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
