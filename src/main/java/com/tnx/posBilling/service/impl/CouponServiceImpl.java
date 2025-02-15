package com.tnx.posBilling.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tnx.posBilling.exceptions.ResourceNotFoundException;
import com.tnx.posBilling.model.Coupon;
import com.tnx.posBilling.model.RedeemCouponRequest;
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
    public ResponseEntity<Coupon> findByCouponCode(String couponcode) {
        Coupon coupon = couponRepository.findByCouponCode(couponcode)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon is not find"));
        return new ResponseEntity<>(coupon, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Double> redeemCoupon(RedeemCouponRequest request) {
        Optional<Coupon> optionalCoupon = couponRepository.findByCouponCode(request.getCouponCode());

        if (optionalCoupon.isEmpty()) {
            throw new ResourceNotFoundException("Invalid coupon code.");
        }

        Coupon coupon = optionalCoupon.get();

        // Check if the coupon has expired
        if (coupon.getEndDate() != null && LocalDateTime.now().isAfter(coupon.getEndDate())) {
            throw new ResourceNotFoundException("Coupon has expired.");
        }

        // Check if usage limit is reached
        int usedCount = Optional.ofNullable(coupon.getUsedCount()).orElse(0);
        if (coupon.getMaximumUseCount() != null && usedCount >= coupon.getMaximumUseCount()) {
            throw new ResourceNotFoundException("Coupon usage limit reached.");
        }

        // Check if order meets the minimum value
        if (coupon.isMinimumRequirement() && request.getOrderValue() < coupon.getMinimumOrderValue()) {
            throw new ResourceNotFoundException("Order value must be at least " + coupon.getMinimumOrderValue());
        }

        // Check if the coupon applies to the customer
        if (coupon.getCustomerType() == Coupon.CustomerType.SEGMENT
                && !"VIP".equalsIgnoreCase(coupon.getSegmentName())) {
            throw new ResourceNotFoundException("Coupon is not valid for this customer segment.");
        }

        if (coupon.getCustomerType() == Coupon.CustomerType.INDIVIDUAL &&
                !Objects.equals(request.getCustomerId(), coupon.getCustomerId())) {
            throw new ResourceNotFoundException("Coupon is not valid for this customer.");
        }

        // Apply the discount
        double discount = coupon.getDiscountInAmount();
        if (coupon.getDiscountInPercent() > 0) {
            discount = (request.getOrderValue() * coupon.getDiscountInPercent()) / 100;
        }

        // Ensure discount is non-negative
        discount = Math.max(0, discount);

        // Update the coupon usage count safely
        coupon.setUsedCount(usedCount + 1);
        couponRepository.save(coupon);

        return new ResponseEntity<>(discount, HttpStatus.OK);
    }
}
