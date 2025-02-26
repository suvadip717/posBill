package com.tnx.posBilling.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CompanyDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
