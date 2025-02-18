package com.tnx.posBilling.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tnx.posBilling.model.RedeemedCoupon;
import com.tnx.posBilling.service.impl.RedeemCouponServiceImpl;

@RestController
@RequestMapping("/redeem-coupon")
@CrossOrigin
public class RedeemCouponController {
    @Autowired
    private RedeemCouponServiceImpl redeemCouponService;

    @GetMapping
    public ResponseEntity<List<RedeemedCoupon>> allCoupons() {
        return redeemCouponService.findAllCoupon();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RedeemedCoupon> couponsById(@PathVariable Long id) {
        return redeemCouponService.findById(id);
    }
}
