package com.tnx.posBilling.repository;

import org.springframework.stereotype.Repository;

import com.tnx.posBilling.model.Coupon;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Integer> {
    List<Coupon> findByCouponCode(String couponCode);
}
