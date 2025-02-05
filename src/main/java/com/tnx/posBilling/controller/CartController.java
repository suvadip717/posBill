package com.tnx.posBilling.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tnx.posBilling.model.Cart;
import com.tnx.posBilling.service.CartService;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping("/calculate")
    public double calculateTotal(@RequestBody Cart cart) {
        return cartService.calculateFinalAmount(cart);
    }

    @PostMapping
    public ResponseEntity<Cart> createCart(@RequestBody Cart cart) {
        return cartService.createCart(cart);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<Cart> getCart(@PathVariable String cartId) {
        return cartService.getCartById(cartId);
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<String> deleteCart(@PathVariable String cartId) {
        return cartService.deleteCart(cartId);
    }

    @GetMapping
    public ResponseEntity<List<Cart>> allCart() {
        return cartService.allCart();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cart> updateCart(@PathVariable String id, @RequestBody Cart newCart) {
        return cartService.updateCart(id, newCart);
    }
}
