package com.tnx.posBilling.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tnx.posBilling.model.Coupon;
import com.tnx.posBilling.model.RedeemedCoupon;
import com.tnx.posBilling.model.User;

@Repository
public interface RedeemedCouponRepository extends JpaRepository<RedeemedCoupon, Long> {
    Optional<RedeemedCoupon> findByCouponAndCustomer(Coupon coupon, User customer);

    boolean existsByCustomerAndCoupon(User customer, Coupon coupon);
}
