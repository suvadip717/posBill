package com.tnx.posBilling.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tnx.posBilling.model.Coupon;
import com.tnx.posBilling.model.RedeemCouponRequest;
import com.tnx.posBilling.service.impl.CouponServiceImpl;

@RestController
@CrossOrigin
@RequestMapping("/coupon")
public class CouponController {
    @Autowired
    private CouponServiceImpl couponService;

    @GetMapping
    public ResponseEntity<List<Coupon>> getAllCoupons() {
        return couponService.getAllCoupons();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Coupon> getCouponById(@PathVariable int id) {
        return couponService.getCouponById(id);
    }

    @PostMapping
    public ResponseEntity<Coupon> createCoupon(@RequestBody Coupon coupon) {
        return couponService.createCoupon(coupon);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Coupon> updateCoupon(@PathVariable int id, @RequestBody Coupon couponDetails) {
        return couponService.updateCoupon(id, couponDetails);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCoupon(@PathVariable int id) {
        return couponService.deleteCoupon(id);
    }

    @GetMapping("/coupon-code/{code}")
    public ResponseEntity<Coupon> findByCouponCode(@PathVariable String code) {
        return couponService.findByCouponCode(code);
    }

    @PostMapping("/redeem-coupon")
    public ResponseEntity<?> redeemCouponCode(@RequestBody RedeemCouponRequest request) {
        return couponService.redeemCoupon(request);
    }
}
