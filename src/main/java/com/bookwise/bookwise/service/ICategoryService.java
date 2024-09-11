package com.bookwise.bookwise.service;

import com.bookwise.bookwise.dto.category.CategoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ICategoryService {

    List<CategoryDTO> getAllCategories(Sort sort);
    Page<CategoryDTO> getCategories(Pageable pageable, String search);

    CategoryDTO getCategoryById(Long id);

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO);

    CategoryDTO deleteCategoryById(Long id);

}
