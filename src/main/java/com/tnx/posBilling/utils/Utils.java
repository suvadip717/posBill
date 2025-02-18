package com.tnx.posBilling.utils;

import java.util.Collections;

import com.tnx.posBilling.dto.CategoryDTO;
import com.tnx.posBilling.dto.CategoryRootDTO;
import com.tnx.posBilling.dto.ProductDTO;
import com.tnx.posBilling.model.Category;
import com.tnx.posBilling.model.Product;

public class Utils {
    public static ProductDTO mapProductdtoToProduct(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductId(product.getProductId());
        productDTO.setBarCode(product.getBarCode());
        productDTO.setProductCode(product.getProductCode());
        productDTO.setProductLabel(product.getProductLabel());
        productDTO.setImageUrl(product.getImageUrl());
        productDTO.setUnitPrice(product.getUnitPrice());
        productDTO.setMrp(product.getMrp());
        productDTO.setDiscountAmount(product.getDiscountAmount());
        productDTO.setDiscountPercentage(product.getDiscountPercentage());
        productDTO.setUnit(product.getUnit());
        productDTO.setSkuCode(product.getSkuCode());
        productDTO.setTaxPercentage(product.getTaxPercentage());
        productDTO.setStockQuantity(product.getStockQuantity());
        productDTO.setCategory(product.getCategory());
        productDTO.setCreatedAt(product.getCreatedAt());
        productDTO.setUpdatedAt(product.getUpdatedAt());
        return productDTO;
    }

    public static CategoryDTO mapCategoryDtoToCategory(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryId(category.getCategoryId());
        categoryDTO.setImageUrl(category.getImageUrl());
        categoryDTO.setCategoryLabel(category.getCategoryLabel());
        categoryDTO.setProducts(category.getProducts());
        categoryDTO.setSubCategories(category.getSubCategories());
        categoryDTO.setParentCategory(category.getParentCategory());
        categoryDTO.setCreatedAt(category.getCreatedAt());
        categoryDTO.setUpdatedAt(category.getUpdatedAt());
        return categoryDTO;
    }

    public static CategoryRootDTO mapCategoryRootDtoToCategory(Category category) {
        CategoryRootDTO categoryDTO = new CategoryRootDTO();
        categoryDTO.setCategoryId(category.getCategoryId());
        categoryDTO.setImageUrl(category.getImageUrl());
        categoryDTO.setCategoryLabel(category.getCategoryLabel());
        // categoryDTO.setProducts(category.getProducts());
        categoryDTO.setSubCategories(Collections.emptyList());
        // categoryDTO.setParentCategory(category.getParentCategory());
        categoryDTO.setCreatedAt(category.getCreatedAt());
        categoryDTO.setUpdatedAt(category.getUpdatedAt());
        return categoryDTO;
    }

}
