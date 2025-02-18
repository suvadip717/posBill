package com.tnx.posBilling.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tnx.posBilling.exceptions.ResourceNotFoundException;
import com.tnx.posBilling.model.RedeemedCoupon;
import com.tnx.posBilling.repository.RedeemedCouponRepository;
import com.tnx.posBilling.service.interfaces.RedeemCouponService;

@Service
public class RedeemCouponServiceImpl implements RedeemCouponService {
    @Autowired
    private RedeemedCouponRepository redeemedCouponRepository;

    @Override
    public ResponseEntity<RedeemedCoupon> findById(Long id) {
        RedeemedCoupon coupon = redeemedCouponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon is not found"));
        return new ResponseEntity<>(coupon, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<RedeemedCoupon>> findAllCoupon() {
        List<RedeemedCoupon> allCoupons = redeemedCouponRepository.findAll();
        return new ResponseEntity<>(allCoupons, HttpStatus.OK);
    }

}
