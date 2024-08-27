package com.bookwise.bookwise.service.impl;

import com.bookwise.bookwise.dto.category.CategoryDTO;
import com.bookwise.bookwise.entity.Category;
import com.bookwise.bookwise.exception.ResourceNotFoundException;
import com.bookwise.bookwise.mapper.CategoryMapper;
import com.bookwise.bookwise.repository.CategoryRepository;
import com.bookwise.bookwise.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements ICategoryService {

    private final CategoryRepository categoryRepository;

//    @Override
//    public List<CategoryDTO> getCategories() {
//        List<Category> categoryList = categoryRepository.findAll();
//        List<CategoryDTO> categoryDTOList = new ArrayList<>();
//        categoryList.forEach(category -> categoryDTOList.add(CategoryMapper.mapToCategoryDTO(category, new CategoryDTO())));
//
//        return categoryDTOList;
//    }

    @Override
    public List<CategoryDTO> getAllCategories(Sort sort) {
        return categoryRepository.findAll(sort).stream()
                .map(category -> CategoryMapper.mapToCategoryDTO(category, new CategoryDTO()))
                .collect(Collectors.toList());
    }

//    @Override
//    public Page<CategoryDTO> getCategories(Pageable pageable) {
//        Page<Category> categoryPage = categoryRepository.findAll(pageable);
//        return categoryPage.map(category -> CategoryMapper.mapToCategoryDTO(category, new CategoryDTO()));
//    }

    @Override
    public Page<CategoryDTO> getCategories(Pageable pageable, String search) {
        Page<Category> categoryPage;
        if (search != null && !search.isEmpty()) {
            categoryPage = categoryRepository.findByNameContainingIgnoreCase(search, pageable);
        } else {
            categoryPage = categoryRepository.findAll(pageable);
        }
        return categoryPage.map(category -> CategoryMapper.mapToCategoryDTO(category, new CategoryDTO()));
    }

    @Override
    public Long getCategoryCount() {
        return categoryRepository.count();
    }

    @Override
    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", id.toString())
        );

        CategoryDTO categoryDTO = CategoryMapper.mapToCategoryDTO(category, new CategoryDTO());
        return categoryDTO;
    }

    @Override
    public CategoryDTO getCategoryByName(String name) {
        Category category = categoryRepository.findByName(name).orElseThrow(
                () -> new ResourceNotFoundException("Category", "name", name)
        );

        CategoryDTO categoryDTO = CategoryMapper.mapToCategoryDTO(category, new CategoryDTO());

        return categoryDTO;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = CategoryMapper.mapToCategory(categoryDTO, new Category());
        Category savedCategory = categoryRepository.save(category);

        CategoryDTO categoryDTONew = CategoryMapper.mapToCategoryDTO(savedCategory, new CategoryDTO());
        return categoryDTONew;
    }

    @Override
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", id.toString())
        );

        category = CategoryMapper.mapToCategory(categoryDTO, category);
        Category updatedCategory = categoryRepository.save(category);

        CategoryDTO categoryDTOUpdated = CategoryMapper.mapToCategoryDTO(updatedCategory, new CategoryDTO());

        return categoryDTOUpdated;
    }

    @Override
    public CategoryDTO deleteCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", id.toString())
        );
        categoryRepository.deleteById(id);
        CategoryDTO categoryDTO = CategoryMapper.mapToCategoryDTO(category, new CategoryDTO());

        return categoryDTO;
    }

    @Override
    public CategoryDTO deleteCategoryByName(String name) {
        Category category = categoryRepository.findByName(name).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "name", name)
        );
        categoryRepository.deleteById(category.getId());
        CategoryDTO categoryDTO = CategoryMapper.mapToCategoryDTO(category, new CategoryDTO());

        return categoryDTO;
    }
}
