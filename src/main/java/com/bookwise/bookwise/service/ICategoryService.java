package com.bookwise.bookwise.service;

import com.bookwise.bookwise.dto.category.CategoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ICategoryService {

//    List<CategoryDTO> getCategories();
    List<CategoryDTO> getAllCategories(Sort sort);
//    Page<CategoryDTO> getCategories(Pageable pageable);
    Page<CategoryDTO> getCategories(Pageable pageable, String search);

    Long getCategoryCount();

    CategoryDTO getCategoryById(Long id);
    CategoryDTO getCategoryByName(String name);

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO);

    CategoryDTO deleteCategoryById(Long id);
    CategoryDTO deleteCategoryByName(String name);

}
