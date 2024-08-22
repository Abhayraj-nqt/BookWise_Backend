package com.bookwise.bookwise.service;

import com.bookwise.bookwise.dto.category.CategoryDTO;

import java.util.List;

public interface ICategoryService {

    List<CategoryDTO> getCategories();
    Long getCategoryCount();

    CategoryDTO getCategoryById(Long id);
    CategoryDTO getCategoryByName(String name);

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO);

    CategoryDTO deleteCategoryById(Long id);
    CategoryDTO deleteCategoryByName(String name);

}
