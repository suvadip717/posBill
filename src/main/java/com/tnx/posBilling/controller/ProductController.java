package com.tnx.posBilling.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.tnx.posBilling.dto.ProductDTO;
import com.tnx.posBilling.model.Product;
import com.tnx.posBilling.service.impl.ProductServiceImpl;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductServiceImpl productService;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable String id) {
        return productService.getProductById(id);
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ProductDTO> createProduct(
            @RequestParam String productId,
            @RequestParam String productCode,
            @RequestParam String barCode,
            @RequestParam String productLabel,
            @RequestParam MultipartFile photo,
            @RequestParam double unitPrice,
            @RequestParam double mrp,
            @RequestParam double discountAmount,
            @RequestParam double discountPercentage,
            @RequestParam String unit,
            @RequestParam String skuCode,
            @RequestParam double taxPercentage,
            @RequestParam Integer stockQuantity,
            @RequestParam String category) {
        return productService.saveProduct(productId, productCode, barCode, productLabel, photo,
                unitPrice, mrp, discountAmount,
                discountPercentage, unit,
                skuCode, taxPercentage, stockQuantity, category);
    }

    // @PostMapping
    // public ProductDTO createProduct(@RequestBody Product product) {
    // return productService.saveProduct(product);
    // }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable String id) {
        return productService.deleteProduct(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable String id,
            @RequestParam String productCode,
            @RequestParam String barCode,
            @RequestParam String productLabel,
            @RequestParam MultipartFile photo,
            @RequestParam double unitPrice,
            @RequestParam double mrp,
            @RequestParam double discountAmount,
            @RequestParam double discountPercentage,
            @RequestParam String unit,
            @RequestParam String skuCode,
            @RequestParam double taxPercentage,
            @RequestParam Integer stockQuantity,
            @RequestParam String category) {
        return productService.updateProduct(id, productCode,
                barCode, productLabel, photo,
                unitPrice, mrp, discountAmount,
                discountPercentage, unit,
                skuCode, taxPercentage, stockQuantity, category);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> getProductByLabelOrBarcode(@RequestParam String search) {
        return productService.findProductFind(search);
    }
}