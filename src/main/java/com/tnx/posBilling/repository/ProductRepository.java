package com.tnx.posBilling.repository;

import java.util.List;
// import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tnx.posBilling.model.Category;
import com.tnx.posBilling.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findByCategory(Category category);

    // List<Product> findByProductLabelOrBarCodeOrProductCode(String productLabel,
    // String barCode, String productCode);

    @Query("SELECT p FROM Product p WHERE LOWER(p.productLabel) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.productCode) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.barCode) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Product> searchSimilarProducts(@Param("keyword") String keyword);
}
