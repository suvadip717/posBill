package com.tnx.posBilling.repository;

import java.util.List;
// import java.util.Optional;

// import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tnx.posBilling.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByParentCategoryIsNull(); // Fetch top-level categories

    List<Category> findByCategoryLabel(String categoryLabel);
}
