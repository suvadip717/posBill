package com.tnx.posBilling.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.tnx.posBilling.dto.CategoryDTO;
import com.tnx.posBilling.model.Category;
import com.tnx.posBilling.service.impl.CategoryServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/categories")
@CrossOrigin
public class CategoryController {
    @Autowired
    private CategoryServiceImpl categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/roots")
    public ResponseEntity<List<Category>> getRootCategories() {
        return categoryService.getRootCategories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }
    // @GetMapping("/{id}")
    // public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
    // return categoryService.getCategoryByIdTest(id);
    // }

    // @PostMapping
    // public Category createCategory(@RequestBody Category category) {
    // return categoryService.saveCategory(category);
    // }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<CategoryDTO> createCategory(
            @RequestParam String categoryLabel,
            @RequestParam MultipartFile imageUrl,
            @RequestParam String parentCategory) {
        return categoryService.saveCategory(categoryLabel, imageUrl, parentCategory);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(
            @PathVariable Long categoryId,
            @RequestParam String categoryLabel,
            @RequestParam MultipartFile imageUrl,
            @RequestParam String parentCategory) {
        return categoryService.updateCategory(categoryId, categoryLabel, imageUrl, parentCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        return categoryService.deleteCategory(id);
    }
}