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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tnx.posBilling.model.Cart;
import com.tnx.posBilling.service.impl.CartServiceImpl;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartServiceImpl cartService;

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

    @PostMapping("/add-product/{cartId}")
    public ResponseEntity<Cart> addProductToCart(
            @PathVariable String cartId,
            @RequestParam String productId,
            @RequestParam int quantity,
            @RequestParam double rate,
            @RequestParam double discount,
            @RequestParam double totalAmount) {

        return cartService.addProductToCart(cartId, productId, quantity, rate,
                discount, totalAmount);
        // return cartService.addProductToCart(cartId, productId, quantity);
    }

    @DeleteMapping("/remove-product/{cartId}")
    public ResponseEntity<Cart> removeProductFromCart(
            @PathVariable String cartId,
            @RequestParam String productId) {

        Cart updatedCart = cartService.removeProductFromCart(cartId, productId);
        return ResponseEntity.ok(updatedCart);
    }

    @PutMapping("/update-product/{cartId}")
    public ResponseEntity<Cart> updateProductFromCart(
            @PathVariable String cartId,
            @RequestParam String productId,
            @RequestParam Integer quantity) {

        return cartService.updateProductQuantityInCart(cartId, productId, quantity);
    }
}
