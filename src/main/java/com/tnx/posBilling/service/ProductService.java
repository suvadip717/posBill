package com.tnx.posBilling.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tnx.posBilling.dto.ProductDTO;
import com.tnx.posBilling.model.Category;
import com.tnx.posBilling.model.Product;
import com.tnx.posBilling.repository.ProductRepository;
import com.tnx.posBilling.utils.Utils;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AwsS3Service aS3Service;

    @Autowired
    private ObjectMapper objectMapper;

    public ResponseEntity<List<Product>> getAllProducts() {
        return new ResponseEntity<>(productRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<ProductDTO> getProductById(String id) {
        ProductDTO productDTO = new ProductDTO();
        try {
            Product newProduct = productRepository.findById(id).get();
            productDTO = Utils.mapProductdtoToProduct(newProduct);
            productDTO.setStatusCode(200);
            productDTO.setMessage("successful");
            return new ResponseEntity<>(productDTO, HttpStatus.FOUND);
        } catch (Exception e) {
            productDTO.setStatusCode(500);
            productDTO.setMessage("Product not found " + e.getMessage());
            throw new ExpressionException("Product is not found " + e.toString());
        }
    }

    // public ProductDTO saveProduct(Product product) {
    // ProductDTO productDTO = new ProductDTO();
    // try {
    // productRepository.save(product);
    // productDTO = Utils.mapProductdtoToProduct(product);
    // productDTO.setStatusCode(200);
    // productDTO.setMessage("successful");
    // } catch (Exception e) {
    // productDTO.setStatusCode(500);
    // productDTO.setMessage("Error saving product " + e.getMessage());
    // }
    // return productDTO;
    // }

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
            productRepository.save(newProduct);
            productDTO = Utils.mapProductdtoToProduct(newProduct);
            productDTO.setStatusCode(200);
            productDTO.setMessage("successful");
            return new ResponseEntity<>(productDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            productDTO.setStatusCode(500);
            productDTO.setMessage("Error saving product " + e.getMessage());
            throw new ExpressionException("Product is not save " + e.toString());
        }
    }

    public ResponseEntity<String> deleteProduct(String id) {
        productRepository.deleteById(id);
        return new ResponseEntity<>("Product deleted successfully", HttpStatus.OK);
    }
}