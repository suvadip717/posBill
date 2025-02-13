package com.tnx.posBilling.service.interfaces;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.tnx.posBilling.model.Coupon;

public interface CouponService {
    public ResponseEntity<List<Coupon>> getAllCoupons();

    public ResponseEntity<Coupon> getCouponById(int id);

    public ResponseEntity<Coupon> createCoupon(Coupon coupon);

    public ResponseEntity<Coupon> updateCoupon(int id, Coupon couponDetails);

    public ResponseEntity<String> deleteCoupon(int id);

    public ResponseEntity<List<Coupon>> findByCouponCode(String couponcode);
}
