package com.cjchika.jobber.category.mapper;

import com.cjchika.jobber.category.dto.CategoryRequestDTO;
import com.cjchika.jobber.category.dto.CategoryResponseDTO;
import com.cjchika.jobber.category.dto.CategoryUpdateDTO;
import com.cjchika.jobber.category.model.Category;

public class CategoryMapper {
    public static CategoryResponseDTO toDTO(Category category){
        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setCreatedAt(category.getCreatedAt());
        dto.setUpdatedAt(category.getUpdatedAt());
        return dto;
    }

    public static Category toModel(CategoryRequestDTO dto) {
        Category category = new Category();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        return category;
    }

    public static void updateModel(CategoryUpdateDTO dto, Category category) {
        if (dto.getName() != null) {
            category.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            category.setDescription(dto.getDescription());
        }
    }
}
