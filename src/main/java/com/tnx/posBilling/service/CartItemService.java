package com.tnx.posBilling.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tnx.posBilling.model.CartItem;
import com.tnx.posBilling.repository.CartItemRepository;

@Service
public class CartItemService {
    @Autowired
    private CartItemRepository cartItemRepository;

    public ResponseEntity<CartItem> addCartItem(CartItem cartItem) {
        return new ResponseEntity<>(cartItemRepository.save(cartItem), HttpStatus.CREATED);
    }

    public Optional<CartItem> getCartItemById(Long cartItemId) {
        return cartItemRepository.findById(cartItemId);
    }

    public void removeCartItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    public ResponseEntity<List<CartItem>> getAllItems() {
        return new ResponseEntity<>(cartItemRepository.findAll(), HttpStatus.OK);
    }
}
