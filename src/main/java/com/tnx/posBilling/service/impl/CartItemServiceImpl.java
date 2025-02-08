package com.tnx.posBilling.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;

import com.tnx.posBilling.model.CartItem;
import com.tnx.posBilling.repository.CartItemRepository;
import com.tnx.posBilling.service.interfaces.CartItemService;

import java.util.List;
import java.util.Optional;

@Service
public class CartItemServiceImpl implements CartItemService {
    @Autowired
    private CartItemRepository cartItemRepository;

    public ResponseEntity<CartItem> addCartItem(CartItem cartItem) {
        return new ResponseEntity<>(cartItemRepository.save(cartItem), HttpStatus.CREATED);
    }

    @Override
    public Optional<CartItem> getCartItemById(Long cartItemId) {
        return cartItemRepository.findById(cartItemId);
    }

    @Override
    public void removeCartItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    public ResponseEntity<List<CartItem>> getAllItems() {
        return new ResponseEntity<>(cartItemRepository.findAll(), HttpStatus.OK);
    }
}
