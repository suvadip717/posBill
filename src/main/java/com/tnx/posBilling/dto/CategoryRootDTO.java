package com.tnx.posBilling.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.tnx.posBilling.model.Category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRootDTO {

    private Long categoryId;
    private String categoryLabel;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CategoryRootDTO> subCategories; // Maintain hierarchy without products

    public static CategoryRootDTO fromEntity(Category category) {
        if (category == null) {
            return null;
        }

        CategoryRootDTO dto = new CategoryRootDTO();
        dto.setCategoryId(category.getCategoryId());
        dto.setCategoryLabel(category.getCategoryLabel());
        dto.setImageUrl(category.getImageUrl());
        dto.setCreatedAt(category.getCreatedAt());
        dto.setUpdatedAt(category.getUpdatedAt());

        // Recursively map subCategories without products
        if (category.getSubCategories() != null) {
            dto.setSubCategories(category.getSubCategories().stream()
                    .map(CategoryRootDTO::fromEntity)
                    .collect(Collectors.toList()));
        } else {
            dto.setSubCategories(new ArrayList<>());
        }

        return dto;
    }
}
