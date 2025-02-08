package com.tnx.posBilling.service.interfaces;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.tnx.posBilling.model.Cart;

public interface CartService {
    public ResponseEntity<Cart> createCart(Cart cart);

    public ResponseEntity<Cart> getCartById(String cartId);

    public ResponseEntity<String> deleteCart(String cartId);

    public ResponseEntity<List<Cart>> allCart();

    public ResponseEntity<Cart> updateCart(String cartId, Cart updatedCart);

    public ResponseEntity<Cart> addProductToCart(String cartId, String productId, int quantity, double rate,
            double discount, double totalAmount);

    public Cart removeProductFromCart(String cartId, String productId);

    public Cart updateProductQuantityInCart(String cartId, String productId, int quantity);
}
