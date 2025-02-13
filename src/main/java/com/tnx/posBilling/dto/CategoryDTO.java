package com.tnx.posBilling.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.tnx.posBilling.model.Category;
import com.tnx.posBilling.model.Product;

import lombok.Data;

@Data
public class CategoryDTO {
    private int statusCode;
    private String message;
    private Long categoryId;
    private String categoryLabel;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Category parentCategory;

    private List<Category> subCategories;

    private List<Product> products;
}