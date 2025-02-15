package com.tnx.posBilling.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String couponCode;

    private String description;
    private double discountInPercent;
    private double discountInAmount;
    private boolean minimumRequirement;
    private double minimumOrderValue;

    private Integer usedCount = 0;
    private Integer maximumUseCount;

    private boolean appliedOncePerCustomer;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    private CustomerType customerType;

    private String segmentName; // Used if customerType = SEGMENT
    private Integer customerId; // Used if customerType = INDIVIDUAL

    @ManyToOne
    @JoinColumn(name = "created_by_user_id", nullable = false)
    private User createdByUser;

    @CreationTimestamp
    private Timestamp createdAt;

    public enum CustomerType {
        ALL, SEGMENT, INDIVIDUAL
    }
}
