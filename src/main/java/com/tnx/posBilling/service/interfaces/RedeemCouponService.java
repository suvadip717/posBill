package com.tnx.posBilling.service.interfaces;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.tnx.posBilling.model.RedeemedCoupon;

public interface RedeemCouponService {
    public ResponseEntity<RedeemedCoupon> findById(Long id);

    public ResponseEntity<List<RedeemedCoupon>> findAllCoupon();
}
