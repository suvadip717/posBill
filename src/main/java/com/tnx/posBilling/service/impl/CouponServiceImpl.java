package com.tnx.posBilling.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tnx.posBilling.exceptions.ResourceNotFoundException;
import com.tnx.posBilling.model.Coupon;
import com.tnx.posBilling.model.RedeemCouponRequest;
import com.tnx.posBilling.model.RedeemedCoupon;
import com.tnx.posBilling.model.User;
import com.tnx.posBilling.repository.CouponRepository;
import com.tnx.posBilling.repository.RedeemedCouponRepository;
import com.tnx.posBilling.repository.UserRepository;
import com.tnx.posBilling.service.interfaces.CouponService;

@Service
public class CouponServiceImpl implements CouponService {
    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedeemedCouponRepository redeemedCouponRepository;

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
    public ResponseEntity<?> redeemCoupon(RedeemCouponRequest request) {
        // Fetch the coupon
        Coupon coupon = couponRepository.findByCouponCode(request.getCouponCode())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid coupon code"));

        // Validate if the coupon is still active
        LocalDateTime now = LocalDateTime.now();
        if (coupon.getStartDate().isAfter(now) || (coupon.getEndDate() != null && coupon.getEndDate().isBefore(now))) {
            return ResponseEntity.badRequest().body("Coupon is expired or not yet active.");
        }

        // Check if maximum usage count is exceeded
        if (coupon.getMaximumUseCount() != null && coupon.getUsedCount() >= coupon.getMaximumUseCount()) {
            return ResponseEntity.badRequest().body("Coupon usage limit exceeded.");
        }

        // Check minimum order value if required
        if (coupon.isMinimumRequirement() && request.getOrderValue() < coupon.getMinimumOrderValue()) {
            return ResponseEntity.badRequest().body("Order value is less than the required minimum.");
        }

        // Validate customer eligibility
        if (!isCustomerEligible(request.getCustomerId(), coupon)) {
            return ResponseEntity.badRequest().body("Customer is not eligible for this coupon.");
        }
        User customer = userRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Check customer used before or not
        if (coupon.isAppliedOncePerCustomer()) {
            boolean alreadyRedeemed = redeemedCouponRepository.existsByCustomerAndCoupon(customer, coupon);
            if (alreadyRedeemed) {
                return ResponseEntity.badRequest().body("Coupon can only be used once per customer.");
            }
        }

        // Check if the customer has already redeemed this coupon before
        RedeemedCoupon redeemedCoupon = new RedeemedCoupon();

        redeemedCoupon.setCoupon(coupon);
        redeemedCoupon.setCustomer(customer);
        redeemedCoupon.setOrderValue(request.getOrderValue());

        // Save redemption record
        redeemedCouponRepository.save(redeemedCoupon);

        // Increment coupon usage count and update
        coupon.setUsedCount(coupon.getUsedCount() + 1);
        couponRepository.save(coupon);

        // Return discount amount
        Map<String, Object> response = new HashMap<>();
        response.put("CouponCode", coupon.getCouponCode());
        response.put("Description", coupon.getDescription());
        response.put("DiscountInPercent", coupon.getDiscountInPercent());
        response.put("DiscountInAmount", coupon.getDiscountInAmount());
        // response.put("DiscountAmount", calculateDiscount(request.getOrderValue(),
        // coupon));
        // response.put("Final Amount",
        // redeemedCoupon.getOrderValue() - calculateDiscount(request.getOrderValue(),
        // coupon));
        return ResponseEntity.ok(response);
    }

    private boolean isCustomerEligible(int customerId, Coupon coupon) {
        switch (coupon.getCustomerType()) {
            case ALL:
                return true;
            case SEGMENT:
                User user = userRepository.findById(customerId)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
                return user.getCustomerGroup() != null &&
                        user.getCustomerGroup().getGroupName().equalsIgnoreCase(coupon.getSegmentName());
            case INDIVIDUAL:
                return coupon.getCustomerId() != null && coupon.getCustomerId().equals(customerId);
            default:
                return false;
        }
    }

    // private double calculateDiscount(double orderValue, Coupon coupon) {
    // if (coupon.getDiscountInPercent() > 0) {
    // return (orderValue * coupon.getDiscountInPercent()) / 100;
    // } else {
    // return Math.min(orderValue, coupon.getDiscountInAmount());
    // }
    // }

    @Override
    public ResponseEntity<?> checkCoupon(RedeemCouponRequest request) {
        // Fetch the coupon
        Coupon coupon = couponRepository.findByCouponCode(request.getCouponCode())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid coupon code"));

        // Validate if the coupon is still active
        LocalDateTime now = LocalDateTime.now();
        if (coupon.getStartDate().isAfter(now) || (coupon.getEndDate() != null &&
                coupon.getEndDate().isBefore(now))) {
            return ResponseEntity.badRequest().body("Coupon is expired or not yet active.");
        }

        // Check if maximum usage count is exceeded
        if (coupon.getMaximumUseCount() != null && coupon.getUsedCount() >= coupon.getMaximumUseCount()) {
            return ResponseEntity.badRequest().body("Coupon usage limit exceeded.");
        }

        // Check minimum order value if required
        if (coupon.isMinimumRequirement() && request.getOrderValue() < coupon.getMinimumOrderValue()) {
            return ResponseEntity.badRequest().body("Order value is less than the required minimum.");
        }

        // Validate customer eligibility
        if (!isCustomerEligible(request.getCustomerId(), coupon)) {
            return ResponseEntity.badRequest().body("Customer is not eligible for this coupon.");
        }
        User customer = userRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Check customer used before or not
        if (coupon.isAppliedOncePerCustomer()) {
            boolean alreadyRedeemed = redeemedCouponRepository.existsByCustomerAndCoupon(customer, coupon);
            if (alreadyRedeemed) {
                return ResponseEntity.badRequest().body("Coupon can only be used once per customer.");
            }
        }

        // Return discount amount
        Map<String, Object> response = new HashMap<>();
        response.put("CouponCode", coupon.getCouponCode());
        response.put("Description", coupon.getDescription());
        response.put("DiscountPercent", coupon.getDiscountInPercent());
        // response.put("DiscountAmount", calculateDiscount(request.getOrderValue(),
        // coupon));
        response.put("DiscountAmount", coupon.getDiscountInAmount());
        // response.put("FinalAmount",
        // request.getOrderValue() - calculateDiscount(request.getOrderValue(),
        // coupon));
        return ResponseEntity.ok(response);
    }

}
