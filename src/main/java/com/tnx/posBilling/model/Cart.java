package com.tnx.posBilling.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.Data;

@Data
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Double totalDiscount;
    private double discountPercent;
    private double discountAmount;
    private double deliveryCharge;
    private double totalSGST;
    private double totalCGST;
    private double totalIGST;
    private double totalTax;
    private Double grandTotal;
    private double roundingOff;
    private double totalTaxableValue;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<CartItem> items;
    @Transient
    private List<TaxDetail> taxDetails;

    // @ManyToOne
    // @JoinColumn(name = "user_id", nullable = false)
    // @JsonBackReference // Avoid infinite recursion when serializing
    // private User user;
}
