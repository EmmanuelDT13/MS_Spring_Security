package com.emmanuel.api.springsecurity.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmanuel.api.springsecurity.dto.SaveCategory;
import com.emmanuel.api.springsecurity.persistence.entity.Category;

import java.util.Optional;

public interface CategoryService {
    Page<Category> findAll(Pageable pageable);

    Optional<Category> findOneById(Long categoryId);

    Category createOne(SaveCategory saveCategory);

    Category updateOneById(Long categoryId, SaveCategory saveCategory);

    Category disableOneById(Long categoryId);
}
