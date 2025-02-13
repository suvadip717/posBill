package com.tnx.posBilling.model;

import java.security.Timestamp;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Cupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String couponCode;
    private String description;
    private double discountInPercent;
    private double discountInAmount;
    private boolean minimumRequirement;
    private double minimumOrderValue;
    private Integer maximumUseCount;
    private boolean appliedOncePerCustomer;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String customerSelection;
    // {"customerType":"all"}
    // {"customerType":"segment","segmentName":""}
    // {"customerType":"individual", customerId:""}

    // private int createdBy;

    @ManyToOne
    // @JoinColumn(name = "createdBy", referencedColumnName = "id")
    private User createdByUser;

    @CreationTimestamp
    private Timestamp createdAt;
}
