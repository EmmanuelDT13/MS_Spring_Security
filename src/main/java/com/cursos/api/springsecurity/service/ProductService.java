package com.cursos.api.springsecurity.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cursos.api.springsecurity.dto.SaveProduct;
import com.cursos.api.springsecurity.persistence.entity.Product;

import java.util.Optional;

public interface ProductService {
    Page<Product> findAll(Pageable pageable);

    Optional<Product> findOneById(Long productId);

    Product createOne(SaveProduct saveProduct);

    Product updateOneById(Long productId, SaveProduct saveProduct);

    Product disableOneById(Long productId);
}
