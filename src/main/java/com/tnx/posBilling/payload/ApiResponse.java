package com.tnx.posBilling.payload;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ApiResponse {
    public String message;
    private boolean success;
    private HttpStatus status;
}
