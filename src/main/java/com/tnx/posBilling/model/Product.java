package com.tnx.posBilling.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "product")
@NoArgsConstructor
public class Product {
    @Id
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

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @JsonBackReference
    private Category category;
}