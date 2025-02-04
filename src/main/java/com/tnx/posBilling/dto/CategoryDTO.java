package com.tnx.posBilling.dto;

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

    private Long parentCategory;

    private List<Category> subCategories;

    private List<Product> products;
}