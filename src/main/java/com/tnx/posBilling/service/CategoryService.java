package com.tnx.posBilling.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tnx.posBilling.dto.CategoryDTO;

import com.tnx.posBilling.model.Category;

import com.tnx.posBilling.repository.CategoryRepository;
import com.tnx.posBilling.utils.Utils;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AwsS3Service awsS3Service;

    @Autowired
    private ObjectMapper objectMapper;

    public CategoryDTO getCategoryById(Long id) {
        CategoryDTO categoryDTO = new CategoryDTO();
        try {
            Category newCategory = categoryRepository.findById(id).get();
            categoryDTO = Utils.mapCategoryDtoToCategory(newCategory);
            categoryDTO.setStatusCode(200);
            categoryDTO.setMessage("successful");
        } catch (Exception e) {
            categoryDTO.setStatusCode(500);
            categoryDTO.setMessage("Category not found " + e.getMessage());
        }
        return categoryDTO;
    }

    // public Category saveCategory(Category category) {
    // return categoryRepository.save(category);
    // }

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
            categoryRepository.save(newCategory);
            categoryDTO = Utils.mapCategoryDtoToCategory(newCategory);
            categoryDTO.setMessage("Success");
            categoryDTO.setStatusCode(200);
            return new ResponseEntity<>(categoryDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            categoryDTO.setMessage("Category is not saved " + e.toString());
            categoryDTO.setStatusCode(500);
            throw new ExpressionException("Category is not save " + e.toString());
        }
    }

    public ResponseEntity<String> deleteCategory(Long id) {
        categoryRepository.deleteById(id);
        return new ResponseEntity<>("Category Deleted successfully", HttpStatus.OK);
    }

    public ResponseEntity<List<Category>> getAllCategories() {
        return new ResponseEntity<>(categoryRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<List<Category>> getRootCategories() {
        return new ResponseEntity<>(categoryRepository.findByParentCategoryIsNull(), HttpStatus.OK);
    }
}
