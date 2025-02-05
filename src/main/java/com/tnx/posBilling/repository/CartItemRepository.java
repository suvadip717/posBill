package com.tnx.posBilling.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tnx.posBilling.model.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

}
