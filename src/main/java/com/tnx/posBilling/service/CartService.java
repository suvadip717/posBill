package com.tnx.posBilling.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tnx.posBilling.exceptions.ResourceNotFoundException;
import com.tnx.posBilling.model.Cart;
import com.tnx.posBilling.model.CartItem;
import com.tnx.posBilling.model.Product;
import com.tnx.posBilling.repository.CartItemRepository;
import com.tnx.posBilling.repository.CartRepository;
import com.tnx.posBilling.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
public class CartService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    public double calculateTotalPrice(Cart cart) {
        return cart.getItems().stream()
                .mapToDouble(item -> {
                    if (item.getDiscount() == 0) {
                        Product product = productRepository.findById(item.getProduct().getProductId())
                                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

                        // Use `unitPrice` instead of `mrp`
                        item.setRate(product.getUnitPrice());

                        // Ensure correct discount application
                        item.setDiscount(product.getDiscountAmount() * item.getQuantity());

                        // Calculate total price per item
                        double totalPrice = (item.getRate() * item.getQuantity());
                        item.setTotalAmount(totalPrice);
                        return totalPrice;
                    }
                    double totalDiscount = item.getDiscount() * item.getQuantity();
                    item.setDiscount(totalDiscount);
                    double totalAmount = item.getRate() * item.getQuantity();
                    item.setTotalAmount(totalAmount);
                    return totalAmount;
                })
                .sum();
    }

    public double calculateTotalDiscount(Cart cart) {
        return cart.getItems().stream()
                // .mapToDouble(item -> item.getDiscount() * item.getQuantity())
                .mapToDouble(item -> item.getDiscount())
                .sum();
    }

    public double calculateFinalAmount(Cart cart) {
        return calculateTotalPrice(cart) + cart.getDeliveryCharge() - cart.getDiscountAmount();
    }

    private void recalculateCart(Cart cart) {
        double totalPrice = 0;
        double totalDiscount = 0;

        for (CartItem item : cart.getItems()) {
            Product product = item.getProduct();

            item.setRate(product.getUnitPrice());
            item.setDiscount(product.getDiscountAmount() * item.getQuantity());
            item.setTotalAmount((item.getRate() * item.getQuantity()));

            totalPrice += item.getTotalAmount();
            totalDiscount += item.getDiscount();
        }
        totalPrice = totalPrice - cart.getDiscountAmount() + cart.getDeliveryCharge();
        cart.setTotalPrice(totalPrice);
        cart.setTotalDiscount(totalDiscount);
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
        cart.setTotalPrice(calculateFinalAmount(cart));
        cart.setTotalDiscount(calculateTotalDiscount(cart));
        return new ResponseEntity<>(cartRepository.save(cart), HttpStatus.CREATED);
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
        existingCart.setTotalPrice(calculateFinalAmount(existingCart));
        existingCart.setDeliveryCharge(updatedCart.getDeliveryCharge());
        existingCart.setDiscountAmount(updatedCart.getDiscountAmount());
        existingCart.setTotalDiscount(calculateTotalDiscount(existingCart));

        cartRepository.save(existingCart);
        return new ResponseEntity<>(existingCart, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Cart> addProductToCart(String cartId, String productId,
            int quantity)
    // public ResponseEntity<Cart> addProductToCart(String cartId, String
    // ,productId,
    // int quantity)
    {
        // Fetch the cart by ID
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        // Fetch the product by ID
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        // Check if the product already exists in the cart
        Optional<CartItem> existingCartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getProductId().equals(productId))
                .findFirst();

        if (existingCartItem.isPresent()) {
            // Update quantity and recalculate totals
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setRate(product.getUnitPrice());
            cartItem.setDiscount(product.getDiscountAmount() * cartItem.getQuantity());
            cartItem.setTotalAmount((cartItem.getRate() * cartItem.getQuantity()) -
                    cartItem.getDiscount());
        } else {
            // Create a new cart item
            CartItem newCartItem = new CartItem();
            newCartItem.setCart(cart);
            newCartItem.setProduct(product);
            newCartItem.setQuantity(quantity);
            newCartItem.setRate(product.getUnitPrice());
            newCartItem.setDiscount(product.getDiscountAmount() * quantity);
            newCartItem.setTotalAmount((newCartItem.getRate() * quantity) -
                    newCartItem.getDiscount());

            cart.getItems().add(newCartItem);
        }

        // Recalculate the cart totals
        recalculateCart(cart);

        return new ResponseEntity<>(cartRepository.save(cart), HttpStatus.OK);
    }

    // @Transactional
    // public ResponseEntity<Cart> addProductToCart(String cartId, String productId,
    // int quantity, double rate,
    // double discount, double totalAmount) {

    // // Fetch the cart by ID
    // Cart cart = cartRepository.findById(cartId)
    // .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

    // // Fetch the product by ID
    // Product product = productRepository.findById(productId)
    // .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

    // // Check if the product already exists in the cart
    // Optional<CartItem> existingCartItem = cart.getItems().stream()
    // .filter(item -> item.getProduct().getProductId().equals(productId))
    // .findFirst();

    // // double finalRate = (rate > 0) ? rate : product.getUnitPrice();
    // // double finalDiscount = (discount > 0) ? discount * quantity :
    // product.getDiscountAmount() * quantity;
    // // double finalTotalAmount = (totalAmount > 0) ? totalAmount : (finalRate *
    // quantity) - finalDiscount;

    // if (existingCartItem.isPresent()) {
    // // Update existing item
    // CartItem cartItem = existingCartItem.get();
    // cartItem.setQuantity(cartItem.getQuantity() + quantity);
    // cartItem.setRate(rate);
    // cartItem.setDiscount(discount);
    // cartItem.setTotalAmount((cartItem.getRate() * cartItem.getQuantity()) -
    // cartItem.getDiscount());
    // } else {
    // // Create a new cart item
    // CartItem newCartItem = new CartItem();
    // newCartItem.setCart(cart);
    // newCartItem.setProduct(product);
    // newCartItem.setQuantity(quantity);
    // newCartItem.setRate(rate);
    // newCartItem.setDiscount(discount);
    // newCartItem.setTotalAmount(totalAmount);

    // cart.getItems().add(newCartItem);
    // }

    // // Recalculate the cart totals
    // recalculateCart(cart);

    // return new ResponseEntity<>(cartRepository.save(cart), HttpStatus.OK);
    // }

    @Transactional
    public Cart removeProductFromCart(String cartId, String productId) {
        // Fetch the cart by ID
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        // Find the cart item associated with the product
        Optional<CartItem> cartItemOptional = cart.getItems().stream()
                .filter(item -> item.getProduct().getProductId().equals(productId))
                .findFirst();

        if (cartItemOptional.isPresent()) {
            CartItem cartItem = cartItemOptional.get();
            cart.getItems().remove(cartItem);
            cartItemRepository.delete(cartItem); // Remove from DB
        } else {
            throw new ResourceNotFoundException("Product not found in cart");
        }

        // Recalculate cart totals after removal
        recalculateCart(cart);

        return cartRepository.save(cart);
    }
}
