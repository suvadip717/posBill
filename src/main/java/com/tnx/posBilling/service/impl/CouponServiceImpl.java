package com.tnx.posBilling.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tnx.posBilling.exceptions.ResourceNotFoundException;
import com.tnx.posBilling.model.Coupon;
import com.tnx.posBilling.repository.CouponRepository;
import com.tnx.posBilling.service.interfaces.CouponService;

@Service
public class CouponServiceImpl implements CouponService {
    @Autowired
    private CouponRepository couponRepository;

    @Override
    public ResponseEntity<List<Coupon>> getAllCoupons() {
        return new ResponseEntity<>(couponRepository.findAll(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Coupon> getCouponById(int id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon is not found"));
        return new ResponseEntity<>(coupon, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Coupon> createCoupon(Coupon coupon) {
        return new ResponseEntity<>(couponRepository.save(coupon), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Coupon> updateCoupon(int id, Coupon couponDetails) {
        return couponRepository.findById(id).map(coupon -> {
            coupon.setCouponCode(couponDetails.getCouponCode());
            coupon.setDescription(couponDetails.getDescription());
            coupon.setDiscountInPercent(couponDetails.getDiscountInPercent());
            coupon.setDiscountInAmount(couponDetails.getDiscountInAmount());
            coupon.setMinimumRequirement(couponDetails.isMinimumRequirement());
            coupon.setMinimumOrderValue(couponDetails.getMinimumOrderValue());
            coupon.setMaximumUseCount(couponDetails.getMaximumUseCount());
            coupon.setAppliedOncePerCustomer(couponDetails.isAppliedOncePerCustomer());
            coupon.setStartDate(couponDetails.getStartDate());
            coupon.setEndDate(couponDetails.getEndDate());
            coupon.setCustomerType(couponDetails.getCustomerType());
            coupon.setSegmentName(couponDetails.getSegmentName());
            coupon.setCustomerId(couponDetails.getCustomerId());
            return new ResponseEntity<>(couponRepository.save(coupon), HttpStatus.OK);
        }).orElseThrow(() -> new ResourceNotFoundException("Coupon not found with id: " + id));
    }

    @Override
    public ResponseEntity<String> deleteCoupon(int id) {
        if (!couponRepository.existsById(id)) {
            throw new ResourceNotFoundException("Coupon not found with id: " + id);
        }
        couponRepository.deleteById(id);
        return new ResponseEntity<>("Coupon deleted", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Coupon>> findByCouponCode(String couponcode) {
        return new ResponseEntity<>(couponRepository.findByCouponCode(couponcode), HttpStatus.OK);
    }
}
