package com.tnx.posBilling.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tnx.posBilling.model.CartItem;
import com.tnx.posBilling.service.CartItemService;

@RestController
@RequestMapping("/cart-items")
public class CartItemController {
    @Autowired
    private CartItemService cartItemService;

    @PostMapping
    public ResponseEntity<CartItem> addCartItem(@RequestBody CartItem cartItem) {
        return cartItemService.addCartItem(cartItem);
    }

    @GetMapping("/{cartItemId}")
    public ResponseEntity<CartItem> getCartItem(@PathVariable Long cartItemId) {
        Optional<CartItem> cartItem = cartItemService.getCartItemById(cartItemId);
        return cartItem.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> removeCartItem(@PathVariable Long cartItemId) {
        cartItemService.removeCartItem(cartItemId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getAllItem() {
        return cartItemService.getAllItems();
    }
}
