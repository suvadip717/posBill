package com.tnx.posBilling.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tnx.posBilling.model.Category;
import com.tnx.posBilling.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findByCategory(Category category);

}
