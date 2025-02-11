package com.tnx.posBilling.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tnx.posBilling.dto.ProductDTO;
import com.tnx.posBilling.exceptions.ResourceNotFoundException;
import com.tnx.posBilling.model.Category;
import com.tnx.posBilling.model.Product;
import com.tnx.posBilling.repository.ProductRepository;
import com.tnx.posBilling.service.AwsS3Service;
import com.tnx.posBilling.service.interfaces.ProductService;
import com.tnx.posBilling.utils.Utils;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AwsS3Service aS3Service;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public ResponseEntity<List<Product>> getAllProducts() {
        return new ResponseEntity<>(productRepository.findAll(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ProductDTO> getProductById(String id) {
        Product newProduct = productRepository.findById(id).orElse(null);
        if (newProduct != null) {
            ProductDTO productDTO = new ProductDTO();
            productDTO = Utils.mapProductdtoToProduct(newProduct);
            productDTO.setStatusCode(200);
            productDTO.setMessage("successful");
            return new ResponseEntity<>(productDTO, HttpStatus.FOUND);
        }
        throw new ResourceNotFoundException("Product Id is not found");
    }

    @Override
    public ResponseEntity<ProductDTO> saveProduct(String productId, String productLabel, MultipartFile photo,
            double unitPrice,
            double mrp,
            double discountAmount, double discountPercentage, String unit, String skuCode, double taxPercentage,
            Integer stockQuantity, String category) {
        ProductDTO productDTO = new ProductDTO();
        try {
            String imageUrl = aS3Service.saveImageToS3(photo);
            Product newProduct = new Product();
            // Mapping string to object
            Category newCategory = objectMapper.readValue(category, Category.class);
            newProduct.setProductId(productId);
            newProduct.setImageUrl(imageUrl);
            newProduct.setProductLabel(productLabel);
            newProduct.setUnitPrice(unitPrice);
            newProduct.setMrp(mrp);
            newProduct.setDiscountAmount(discountAmount);
            newProduct.setDiscountPercentage(discountPercentage);
            newProduct.setUnit(unit);
            newProduct.setSkuCode(skuCode);
            newProduct.setTaxPercentage(taxPercentage);
            newProduct.setStockQuantity(stockQuantity);
            newProduct.setCategory(newCategory);
            newProduct.setCreatedAt(LocalDateTime.now());
            productRepository.save(newProduct);
            productDTO = Utils.mapProductdtoToProduct(newProduct);
            productDTO.setStatusCode(200);
            productDTO.setMessage("successful");
            return new ResponseEntity<>(productDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            productDTO.setStatusCode(500);
            productDTO.setMessage("Error saving product " + e.getMessage());
            throw new RuntimeException("Product is not save " + e.toString());
        }
    }

    @Override
    public ResponseEntity<ProductDTO> updateProduct(String productId, String productLabel, MultipartFile photo,
            double unitPrice,
            double mrp,
            double discountAmount, double discountPercentage, String unit, String skuCode, double taxPercentage,
            Integer stockQuantity, String category) {
        ProductDTO productDTO = new ProductDTO();
        try {
            String imageUrl = aS3Service.saveImageToS3(photo);
            Product existProduct = productRepository.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product is not found"));
            // Mapping string to object
            Category newCategory = objectMapper.readValue(category, Category.class);
            existProduct.setProductId(productId);
            existProduct.setImageUrl(imageUrl);
            existProduct.setProductLabel(productLabel);
            existProduct.setUnitPrice(unitPrice);
            existProduct.setMrp(mrp);
            existProduct.setDiscountAmount(discountAmount);
            existProduct.setDiscountPercentage(discountPercentage);
            existProduct.setUnit(unit);
            existProduct.setSkuCode(skuCode);
            existProduct.setTaxPercentage(taxPercentage);
            existProduct.setStockQuantity(stockQuantity);
            existProduct.setCategory(newCategory);
            existProduct.setUpdatedAt(LocalDateTime.now());
            productRepository.save(existProduct);
            productDTO = Utils.mapProductdtoToProduct(existProduct);
            productDTO.setStatusCode(200);
            productDTO.setMessage("successful");
            return new ResponseEntity<>(productDTO, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            productDTO.setStatusCode(500);
            productDTO.setMessage("Error saving product " + e.getMessage());
            throw new RuntimeException("Product is not save " + e.toString());
        }
    }

    @Override
    public ResponseEntity<String> deleteProduct(String id) {
        productRepository.deleteById(id);
        return new ResponseEntity<>("Product deleted successfully", HttpStatus.OK);
    }
}
