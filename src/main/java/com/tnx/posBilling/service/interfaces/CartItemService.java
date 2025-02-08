package com.tnx.posBilling.service.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.tnx.posBilling.model.CartItem;

public interface CartItemService {
    public ResponseEntity<CartItem> addCartItem(CartItem cartItem);

    public Optional<CartItem> getCartItemById(Long cartItemId);

    public void removeCartItem(Long cartItemId);

    public ResponseEntity<List<CartItem>> getAllItems();
}
