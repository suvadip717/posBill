package com.tnx.posBilling.service.interfaces;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.tnx.posBilling.dto.ProductDTO;
import com.tnx.posBilling.model.Product;

public interface ProductService {
        public ResponseEntity<List<Product>> getAllProducts();

        public ResponseEntity<ProductDTO> getProductById(String id);

        public ResponseEntity<ProductDTO> saveProduct(String productId, String productLabel, String productCode,
                        String barCode, MultipartFile photo,
                        double unitPrice,
                        double mrp,
                        double discountAmount, double discountPercentage, String unit, String skuCode,
                        double taxPercentage,
                        Integer stockQuantity, String category);

        public ResponseEntity<String> deleteProduct(String id);

        public ResponseEntity<ProductDTO> updateProduct(String productId,
                        String productCode,
                        String barCode,
                        String productLabel,
                        MultipartFile photo,
                        double unitPrice,
                        double mrp,
                        double discountAmount, double discountPercentage, String unit, String skuCode,
                        double taxPercentage,
                        Integer stockQuantity, String category);

        public ResponseEntity<List<ProductDTO>> findProductFind(String searchTerm);
}
