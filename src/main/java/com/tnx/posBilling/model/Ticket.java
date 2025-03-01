package com.tnx.posBilling.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, updatable = false, length = 30)
    private String referenceId;
    @Column(length = 20)
    private String email;
    @Column(length = 20)
    private String phoneNumber;
    @Column(length = 30)
    private String subject;
    @Column(length = 30)
    private String issue;
    private String subIssue;
    @Column(length = 100)
    private String description;
    private String attachMent;
    @Column(length = 40)
    private String status;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private LocalDate expectedDate;
    private LocalDate resolvedAt;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "created_by_user_id", nullable = false)
    private User createdByUser;

    // @ManyToOne
    // @JoinColumn(name = "company_id")
    // private Company company;
}
