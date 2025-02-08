package com.tnx.posBilling.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tnx.posBilling.dto.CategoryDTO;
import com.tnx.posBilling.exceptions.ResourceNotFoundException;
import com.tnx.posBilling.model.Category;
import com.tnx.posBilling.repository.CategoryRepository;
import com.tnx.posBilling.service.AwsS3Service;
import com.tnx.posBilling.service.interfaces.CategoryService;
import com.tnx.posBilling.utils.Utils;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AwsS3Service awsS3Service;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public ResponseEntity<CategoryDTO> getCategoryById(Long id) {
        CategoryDTO categoryDTO = new CategoryDTO();
        Category newCategory = categoryRepository.findById(id).orElse(null);
        if (newCategory != null) {
            categoryDTO = Utils.mapCategoryDtoToCategory(newCategory);
            categoryDTO.setStatusCode(200);
            categoryDTO.setMessage("successful");
            return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
        }
        throw new ResourceNotFoundException("Category Id is not found");
    }

    // public Category saveCategory(Category category) {
    // return categoryRepository.save(category);
    // }

    @Override
    public ResponseEntity<CategoryDTO> saveCategory(String categoryLabel, MultipartFile imageUrl,
            String parentCategory) {
        CategoryDTO categoryDTO = new CategoryDTO();
        try {
            Category newCategory = new Category();
            String photoUrl = awsS3Service.saveImageToS3(imageUrl);
            // Mapping parent category string to object
            Category myCategory = objectMapper.readValue(parentCategory, Category.class);
            newCategory.setCategoryLabel(categoryLabel);
            newCategory.setImageUrl(photoUrl);
            newCategory.setParentCategory(myCategory);
            newCategory.setCreatedAt(LocalDateTime.now());
            categoryRepository.save(newCategory);
            categoryDTO = Utils.mapCategoryDtoToCategory(newCategory);
            categoryDTO.setMessage("Success");
            categoryDTO.setStatusCode(200);
            return new ResponseEntity<>(categoryDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            categoryDTO.setMessage("Category is not saved " + e.toString());
            categoryDTO.setStatusCode(500);
            throw new RuntimeException("Category is not save " + e.toString());
        }
    }

    @Override
    public ResponseEntity<CategoryDTO> updateCategory(Long categoryId, String categoryLabel, MultipartFile imageUrl,
            String parentCategory) {
        CategoryDTO categoryDTO = new CategoryDTO();
        try {
            Category existCategory = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category is not found"));
            String photoUrl = awsS3Service.saveImageToS3(imageUrl);
            // Mapping parent category string to object
            Category myCategory = objectMapper.readValue(parentCategory, Category.class);
            existCategory.setCategoryLabel(categoryLabel);
            existCategory.setImageUrl(photoUrl);
            existCategory.setParentCategory(myCategory);
            existCategory.setUpdatedAt(LocalDateTime.now());
            categoryRepository.save(existCategory);
            categoryDTO = Utils.mapCategoryDtoToCategory(existCategory);
            categoryDTO.setMessage("Success");
            categoryDTO.setStatusCode(200);
            return new ResponseEntity<>(categoryDTO, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            categoryDTO.setMessage("Category is not saved " + e.toString());
            categoryDTO.setStatusCode(500);
            throw new RuntimeException("Category is not save " + e.toString());
        }
    }

    @Override
    public ResponseEntity<String> deleteCategory(Long id) {
        categoryRepository.deleteById(id);
        return new ResponseEntity<>("Category Deleted successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Category>> getAllCategories() {
        return new ResponseEntity<>(categoryRepository.findAll(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Category>> getRootCategories() {
        return new ResponseEntity<>(categoryRepository.findByParentCategoryIsNull(), HttpStatus.OK);
    }
}
