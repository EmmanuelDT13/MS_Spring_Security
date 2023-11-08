package com.cursos.api.springsecurity.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.cursos.api.springsecurity.dto.SaveCategory;
import com.cursos.api.springsecurity.persistence.entity.Category;
import com.cursos.api.springsecurity.service.CategoryService;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    @PreAuthorize("hasAuthority('READ_ALL_CATEGORIES')")
    public ResponseEntity<Page<Category>> findAll(Pageable pageable){

        Page<Category> categoriesPage = categoryService.findAll(pageable);
        if(categoriesPage.hasContent()){
            return ResponseEntity.ok(categoriesPage);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{categoryId}")
    @PreAuthorize("hasAuthority('READ_ONE_CATEGORY')")
    public ResponseEntity<Category> findOneById(@PathVariable Long categoryId){

        Optional<Category> category = categoryService.findOneById(categoryId);

        if(category.isPresent()){
            return ResponseEntity.ok(category.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_ONE_CATEGORY')")
    public ResponseEntity<Category> createOne(@RequestBody @Valid SaveCategory saveCategory){
        Category category = categoryService.createOne(saveCategory);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @PutMapping("/{categoryId}")
    @PreAuthorize("hasAuthority('UPDATE_ONE_CATEGORY')")
    public ResponseEntity<Category> updateOneById(@PathVariable Long categoryId, @RequestBody @Valid SaveCategory saveCategory){
        Category category = categoryService.updateOneById(categoryId, saveCategory);
        return ResponseEntity.ok(category);
    }

    @PutMapping("/{categoryId}/disabled")
    @PreAuthorize("hasAuthority('DISABLE_ONE_CATEGORY')")
    public ResponseEntity<Category> disableOneById(@PathVariable Long categoryId){
        Category category = categoryService.disableOneById(categoryId);
        return ResponseEntity.ok(category);
    }

}
