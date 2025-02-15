package com.tnx.posBilling.model;

import lombok.Data;

@Data
public class RedeemCouponRequest {
    private String couponCode;
    private int customerId;
    private double orderValue;
}
