package com.tnx.posBilling.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tnx.posBilling.model.Cart;
import com.tnx.posBilling.service.interfaces.CartService;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.tnx.posBilling.exceptions.ResourceNotFoundException;
import com.tnx.posBilling.model.CartItem;
import com.tnx.posBilling.model.Product;
import com.tnx.posBilling.repository.CartItemRepository;
import com.tnx.posBilling.repository.CartRepository;
import com.tnx.posBilling.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    public double calculateTotalDiscount(Cart cart) {
        return cart.getItems().stream().mapToDouble(CartItem::getTotalDiscount).sum();
    }

    public double calculateTotalTex(Cart cart) {
        return cart.getItems().stream().mapToDouble(CartItem::getTaxAmount).sum();
    }

    public double calculateTotalCGST(Cart cart) {
        return cart.getItems().stream().mapToDouble(CartItem::getCGST).sum();
    }

    public double calculateTotalSGST(Cart cart) {
        return cart.getItems().stream().mapToDouble(CartItem::getSGST).sum();
    }

    public double calculateTotalIGST(Cart cart) {
        return cart.getItems().stream().mapToDouble(CartItem::getIGST).sum();
    }

    public double calculateFinalAmount(Cart cart) {
        return cart.getItems().stream().mapToDouble(CartItem::getTotalAmount).sum()
                + cart.getDeliveryCharge() - cart.getDiscountAmount();
    }

    public double discountAmountPercent(Cart cart) {
        double totalAmount = calculateFinalAmount(cart) - cart.getDeliveryCharge();
        return totalAmount * cart.getDiscountPercent() / 100;
    }

    // create new cart
    @Transactional
    @Override
    public ResponseEntity<Cart> createCart(Cart cart) {
        List<CartItem> updatedItems = cart.getItems().stream().map(item -> {
            Product product = productRepository.findById(item.getProduct().getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Product not found: " + item.getProduct().getProductId()));
            double discountAmount = product.getUnitPrice() * product.getDiscountPercentage() / 100;
            double totalDiscountAmount = discountAmount * item.getQuantity();
            if (item.getDiscountPercentage() == 0) {
                discountAmount = product.getDiscountAmount();
                totalDiscountAmount = product.getDiscountAmount() * item.getQuantity();
            }
            double taxAbleAmount = (product.getUnitPrice() - discountAmount) * item.getQuantity();
            double taxAmount = taxAbleAmount * (product.getTaxPercentage() / 100);
            double totalAmount = taxAbleAmount + taxAmount;

            item.setProduct(product);
            item.setDiscountPercentage(product.getDiscountPercentage());
            item.setDiscount(discountAmount);
            item.setRate(product.getUnitPrice());
            item.setTotalDiscount(totalDiscountAmount);
            item.setTaxableValue(taxAbleAmount);
            item.setTaxPercnt(product.getTaxPercentage());
            item.setTaxAmount(taxAmount);
            item.setCGST(taxAmount / 2);
            item.setSGST(taxAmount / 2);
            item.setIGST(0.0);
            item.setTotalAmount(totalAmount);
            return item;
        }).collect(Collectors.toList());

        cart.setItems(updatedItems);
        cart.setTotalDiscount(calculateTotalDiscount(cart));
        cart.setTotalTax(calculateTotalTex(cart));
        if (cart.getDiscountPercent() != 0) {
            cart.setDiscountAmount(discountAmountPercent(cart));
        }
        cart.setTotalCGST(calculateTotalCGST(cart));
        cart.setTotalSGST(calculateTotalSGST(cart));
        cart.setTotalIGST(calculateTotalIGST(cart));
        cart.setDiscountAmount(cart.getDiscountAmount());
        cart.setDeliveryCharge(cart.getDeliveryCharge());
        cart.setTotalAmount(calculateFinalAmount(cart));
        cart.setCreatedAt(LocalDateTime.now());

        return new ResponseEntity<>(cartRepository.save(cart), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Cart> getCartById(String cartId) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart != null) {
            return new ResponseEntity<>(cart, HttpStatus.OK);
        }
        throw new ResourceNotFoundException("Cart id is not found");
    }

    @Override
    public ResponseEntity<String> deleteCart(String cartId) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart != null) {
            cartRepository.deleteById(cartId);
            return new ResponseEntity<>("Cart deleted", HttpStatus.OK);
        }
        throw new ResourceNotFoundException("Cart id is not found");
    }

    @Override
    public ResponseEntity<List<Cart>> allCart() {
        return new ResponseEntity<>(cartRepository.findAll(), HttpStatus.OK);
    }

    @Transactional
    @Override
    public ResponseEntity<Cart> updateCart(String cartId, Cart updatedCart) {
        Cart existingCart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        // Clear existing items safely
        existingCart.getItems().clear();

        List<CartItem> updatedItems = updatedCart.getItems().stream().map(item -> {
            Product product = productRepository.findById(item.getProduct().getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Product not found: " + item.getProduct().getProductId()));

            // double discountAmount = product.getUnitPrice() *
            // product.getDiscountPercentage() / 100;
            double discountAmount = item.getRate() * item.getDiscountPercentage() / 100;
            double totalDiscountAmount = discountAmount * item.getQuantity();
            // double taxAbleAmount = (product.getUnitPrice() - discountAmount) *
            // item.getQuantity();
            if (item.getDiscountPercentage() == 0) {
                discountAmount = product.getDiscountAmount();
                totalDiscountAmount = product.getDiscountAmount() * item.getQuantity();
            }
            double taxAbleAmount = (item.getRate() - discountAmount) * item.getQuantity();
            double taxAmount = taxAbleAmount * (item.getTaxPercnt() / 100);
            double totalAmount = taxAbleAmount + taxAmount;

            item.setProduct(product);
            item.setCart(existingCart);
            // item.setDiscountPercentage(product.getDiscountPercentage());
            item.setDiscount(discountAmount);
            // item.setRate(product.getUnitPrice());
            item.setTotalDiscount(totalDiscountAmount);
            item.setTaxableValue(taxAbleAmount);
            // item.setTaxPercnt(product.getTaxPercentage());
            item.setTaxAmount(taxAmount);
            item.setCGST(taxAmount / 2);
            item.setSGST(taxAmount / 2);
            item.setIGST(0.0);
            item.setTotalAmount(totalAmount);
            return item;
        }).collect(Collectors.toList());

        // Add the updated items
        existingCart.getItems().addAll(updatedItems);

        // Recalculate totals
        existingCart.setTotalDiscount(calculateTotalDiscount(existingCart));
        existingCart.setTotalTax(calculateTotalTex(existingCart));
        existingCart.setTotalCGST(calculateTotalCGST(existingCart));
        existingCart.setTotalSGST(calculateTotalSGST(existingCart));
        existingCart.setTotalIGST(calculateTotalIGST(existingCart));
        existingCart.setDiscountPercent(updatedCart.getDiscountPercent());
        existingCart.setDeliveryCharge(updatedCart.getDeliveryCharge());
        if (updatedCart.getDiscountPercent() != 0) {
            existingCart.setDiscountAmount(discountAmountPercent(updatedCart));
        } else {
            existingCart.setDiscountAmount(updatedCart.getDiscountAmount());
        }
        // existingCart.setDiscountAmount(updatedCart.getDiscountAmount());
        existingCart.setTotalAmount(calculateFinalAmount(existingCart));
        existingCart.setUpdatedAt(LocalDateTime.now());

        return new ResponseEntity<>(cartRepository.save(existingCart), HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<Cart> addProductToCart(String cartId, String productId, int quantity, double rate,
            double discountPercentage, double discount, double totalAmount, double taxPercnt) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getProductId().equals(productId))
                .findFirst()
                .orElseGet(() -> {
                    CartItem newItem = new CartItem();
                    newItem.setCart(cart);
                    newItem.setProduct(product);
                    cart.getItems().add(newItem);
                    return newItem;
                });

        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        // cartItem.setRate(product.getUnitPrice());
        cartItem.setRate(rate);
        // double discountAmount = product.getUnitPrice() *
        // product.getDiscountPercentage() / 100;
        // double discountAmount = product.getUnitPrice() * discountPercentage / 100;
        double discountAmount = rate * discountPercentage / 100;
        if (discountPercentage == 0) {
            discountAmount = discount;
        }
        cartItem.setDiscount(discountAmount);
        // cartItem.setDiscountPercentage(product.getDiscountPercentage());
        cartItem.setDiscountPercentage(discountPercentage);
        cartItem.setTotalDiscount(discountAmount * cartItem.getQuantity());
        // double taxAbleAmount = (product.getUnitPrice() - discountAmount) *
        // cartItem.getQuantity();
        double taxAbleAmount = (rate - discountAmount) * cartItem.getQuantity();
        // double taxAmount = taxAbleAmount * (product.getTaxPercentage() / 100);
        double taxAmount = taxAbleAmount * (taxPercnt / 100);
        cartItem.setTaxableValue(taxAbleAmount);
        cartItem.setTaxPercnt(product.getTaxPercentage());
        cartItem.setTaxAmount(taxAmount);
        cartItem.setCGST(taxAmount / 2);
        cartItem.setSGST(taxAmount / 2);
        cartItem.setIGST(0.0);
        cartItem.setTotalAmount(taxAbleAmount + taxAmount);

        cart.setTotalDiscount(calculateTotalDiscount(cart));
        cart.setTotalTax(calculateTotalTex(cart));
        cart.setTotalCGST(calculateTotalCGST(cart));
        cart.setTotalSGST(calculateTotalSGST(cart));
        cart.setTotalIGST(calculateTotalIGST(cart));
        cart.setDiscountAmount(cart.getDiscountAmount());
        cart.setDeliveryCharge(cart.getDeliveryCharge());
        cart.setTotalAmount(calculateFinalAmount(cart));
        cart.setUpdatedAt(LocalDateTime.now());
        return new ResponseEntity<>(cartRepository.save(cart), HttpStatus.OK);
    }

    @Transactional
    @Override
    public Cart removeProductFromCart(String cartId, String productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in cart"));

        cart.getItems().remove(cartItem);
        cartItemRepository.delete(cartItem);

        cart.setTotalDiscount(cart.getItems().stream().mapToDouble(CartItem::getTotalDiscount).sum());
        cart.setTotalTax(calculateTotalTex(cart));
        cart.setTotalCGST(calculateTotalCGST(cart));
        cart.setTotalSGST(calculateTotalSGST(cart));
        cart.setTotalIGST(calculateTotalIGST(cart));
        cart.setTotalAmount(calculateFinalAmount(cart));
        cart.setUpdatedAt(LocalDateTime.now());

        return cartRepository.save(cart);
    }

    @Transactional
    @Override
    public ResponseEntity<Cart> updateProductQuantityInCart(String cartId, String productId, int quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in cart"));

        if (quantity <= 0) {
            cart.getItems().remove(cartItem);
            cartItemRepository.delete(cartItem);
        } else {
            double discountAmount = cartItem.getRate() * cartItem.getDiscountPercentage() / 100;
            double totalDiscountAmount = discountAmount * quantity;
            double taxAbleAmount = (cartItem.getRate() - discountAmount) * quantity;
            double taxAmount = taxAbleAmount * (cartItem.getTaxPercnt() / 100);
            double totalAmount = taxAbleAmount + taxAmount;

            cartItem.setQuantity(quantity);
            cartItem.setTotalDiscount(totalDiscountAmount);
            cartItem.setTaxableValue(taxAbleAmount);
            cartItem.setTaxAmount(taxAmount);
            cartItem.setCGST(taxAmount / 2);
            cartItem.setSGST(taxAmount / 2);
            cartItem.setIGST(0.0);
            cartItem.setTotalAmount(totalAmount);
        }

        cart.setTotalDiscount(calculateTotalDiscount(cart));
        cart.setTotalTax(calculateTotalTex(cart));
        cart.setTotalAmount(calculateFinalAmount(cart));
        cart.setTotalCGST(calculateTotalCGST(cart));
        cart.setTotalSGST(calculateTotalSGST(cart));
        cart.setTotalIGST(calculateTotalIGST(cart));
        cart.setUpdatedAt(LocalDateTime.now());

        return new ResponseEntity<>(cartRepository.save(cart), HttpStatus.OK);
    }
}
