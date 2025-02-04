package com.tnx.posBilling.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.tnx.posBilling.model.Category;

import lombok.Data;

@Data
public class ProductDTO {
    private int statusCode;
    private String message;
    private String productId;
    private String productLabel;
    private String imageUrl;
    private double unitPrice;
    private double mrp;
    private double discountAmount;
    private double discountPercentage;
    private String unit;
    private String skuCode;
    private double taxPercentage;
    private Integer stockQuantity;
    @JsonBackReference
    private Category category;
}
