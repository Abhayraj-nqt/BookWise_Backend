package com.bookwise.bookwise.mapper;

import com.bookwise.bookwise.dto.category.CategoryDTO;
import com.bookwise.bookwise.entity.Category;

public final class CategoryMapper {

    public static CategoryDTO mapToCategoryDTO(Category category, CategoryDTO categoryDTO) {
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());

        return categoryDTO;
    }

    public static Category mapToCategory(CategoryDTO categoryDTO, Category category) {
//        category.setId(categoryDTO.getId());
        category.setName(categoryDTO.getName());
        return category;
    }

}
