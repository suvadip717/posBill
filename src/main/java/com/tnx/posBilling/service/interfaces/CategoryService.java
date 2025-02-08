package com.tnx.posBilling.service.interfaces;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.tnx.posBilling.dto.CategoryDTO;
import com.tnx.posBilling.model.Category;

public interface CategoryService {
    public ResponseEntity<CategoryDTO> getCategoryById(Long id);

    public ResponseEntity<CategoryDTO> saveCategory(String categoryLabel, MultipartFile imageUrl,
            String parentCategory);

    public ResponseEntity<CategoryDTO> updateCategory(Long categoryId, String categoryLabel, MultipartFile imageUrl,
            String parentCategory);

    public ResponseEntity<String> deleteCategory(Long id);

    public ResponseEntity<List<Category>> getAllCategories();

    public ResponseEntity<List<Category>> getRootCategories();

}
