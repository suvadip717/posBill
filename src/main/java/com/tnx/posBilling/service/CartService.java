package com.tnx.posBilling.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tnx.posBilling.exceptions.ResourceNotFoundException;
import com.tnx.posBilling.model.Cart;
import com.tnx.posBilling.model.CartItem;
import com.tnx.posBilling.model.Product;
import com.tnx.posBilling.repository.CartRepository;
import com.tnx.posBilling.repository.ProductRepository;

@Service
public class CartService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    public double calculateTotalPrice(Cart cart) {
        return cart.getItems().stream()
                .mapToDouble(item -> {
                    Product product = productRepository.findById(item.getProduct().getProductId())
                            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
                    return product.getMrp() * item.getQuantity();
                })
                .sum();
    }

    public double calculateTotalDiscount(Cart cart) {
        return cart.getItems().stream()
                .mapToDouble(item -> {
                    Product product = productRepository.findById(item.getProduct().getProductId())
                            .orElseThrow(() -> new RuntimeException("Product not found"));
                    return product.getDiscountAmount() * item.getQuantity();
                })
                .sum();
    }

    public double calculateFinalAmount(Cart cart) {
        return calculateTotalPrice(cart) - calculateTotalDiscount(cart) + cart.getDeliveryCharge()
                - cart.getDiscountAmount();
    }

    public ResponseEntity<Cart> createCart(Cart cart) {
        List<CartItem> updatedItems = cart.getItems().stream().map(item -> {
            Product product = productRepository.findById(item.getProduct().getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Product not found: " + item.getProduct().getProductId()));
            item.setProduct(product);
            return item;
        }).collect(Collectors.toList());

        cart.setItems(updatedItems);
        cart.setTotalDiscount(calculateTotalDiscount(cart));
        cart.setTotalPrice(calculateFinalAmount(cart));
        return new ResponseEntity<>(cartRepository.save(cart), HttpStatus.CREATED);
        // return new ResponseEntity<>(cart, HttpStatus.CREATED);
    }

    public ResponseEntity<Cart> getCartById(String cartId) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart != null) {
            return new ResponseEntity<>(cart, HttpStatus.OK);
        }
        throw new ResourceNotFoundException("Cart id is not found");
    }

    public ResponseEntity<String> deleteCart(String cartId) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart != null) {
            cartRepository.deleteById(cartId);
            return new ResponseEntity<>("Cart deleted", HttpStatus.OK);
        }
        throw new ResourceNotFoundException("Cart id is not found");
    }

    public ResponseEntity<List<Cart>> allCart() {
        return new ResponseEntity<>(cartRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<Cart> updateCart(String cartId, Cart updatedCart) {
        Cart existingCart = cartRepository.findById(cartId).orElse(null);
        if (existingCart == null) {
            throw new ResourceNotFoundException("Cart not found");
        }

        // Clear existing items while avoiding orphan removal issues
        existingCart.getItems().forEach(item -> item.setCart(null));
        existingCart.getItems().clear();

        List<CartItem> updatedItems = updatedCart.getItems().stream().map(item -> {
            Product product = productRepository.findById(item.getProduct().getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Product not found: " + item.getProduct().getProductId()));
            item.setProduct(product);
            item.setCart(existingCart);
            return item;
        }).collect(Collectors.toList());

        // Add the updated items
        existingCart.getItems().addAll(updatedItems);

        // Recalculate total price and discount
        existingCart.setTotalDiscount(calculateTotalDiscount(existingCart));
        existingCart.setTotalPrice(calculateFinalAmount(existingCart));
        existingCart.setDeliveryCharge(updatedCart.getDeliveryCharge());
        existingCart.setDiscountAmount(updatedCart.getDiscountAmount());

        cartRepository.save(existingCart);
        return new ResponseEntity<>(existingCart, HttpStatus.OK);
    }

}
