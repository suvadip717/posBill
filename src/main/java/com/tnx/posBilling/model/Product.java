package com.tnx.posBilling.model;

import java.time.LocalDateTime;

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
    @Column(length = 30)
    private String productId;

    @Column(length = 30)
    private String productCode;
    @Column(length = 40)
    private String barCode;
    @Column(length = 40)
    private String productLabel;
    private String imageUrl;
    private double unitPrice;
    private double mrp;
    private double discountAmount;
    private double discountPercentage;
    @Column(length = 20)
    private String unit;
    @Column(length = 40)
    private String skuCode;
    private double taxPercentage;
    private Integer stockQuantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @JsonBackReference
    private Category category;
}