package com.bookwise.bookwise.controller;

import com.bookwise.bookwise.dto.category.CategoryDTO;
import com.bookwise.bookwise.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path="/api", produces = {MediaType.APPLICATION_JSON_VALUE})
public class CategoryController {

    private final ICategoryService iCategoryService;

//    @GetMapping("/categories")
//    public ResponseEntity<List<CategoryDTO>> getCategories() {
//        List<CategoryDTO> categoryDTOList = iCategoryService.getCategories();
//        return ResponseEntity.status(HttpStatus.OK).body(categoryDTOList);
//    }

    @GetMapping("/categories")
    public ResponseEntity<?> getCategories(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search) {

        if (page == null || size == null) {
            // Fetch all categories without pagination
            List<CategoryDTO> categoryDTOList = iCategoryService.getAllCategories(Sort.by(Sort.Direction.fromString(sortDir), sortBy));
            return ResponseEntity.ok(categoryDTOList);
        } else {
            // Apply pagination
//            Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(sortDir), sortBy);
//            Page<CategoryDTO> categoryDTOPage = iCategoryService.getCategories(pageable);
//            return ResponseEntity.ok(categoryDTOPage);

            Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(sortDir), sortBy);
            Page<CategoryDTO> categoryDTOPage = iCategoryService.getCategories(pageable, search);
            return ResponseEntity.status(HttpStatus.OK).body(categoryDTOPage);
        }
    }

    @GetMapping("/category-count")
    public ResponseEntity<Long> getCategoryCount() {
        Long categoryCount = iCategoryService.getCategoryCount();
        return ResponseEntity.status(HttpStatus.OK).body(categoryCount);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable Long id) {
        CategoryDTO categoryDTO = iCategoryService.getCategoryById(id);
        return ResponseEntity.status(HttpStatus.OK).body(categoryDTO);
    }



    @PostMapping("/category")
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        CategoryDTO savedCategoryDTO = iCategoryService.createCategory(categoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategoryDTO);
    }

    @PutMapping("/category/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO updatedCategoryDTO = iCategoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedCategoryDTO);
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long id) {
        CategoryDTO categoryDTO = iCategoryService.deleteCategoryById(id);
        return ResponseEntity.status(HttpStatus.OK).body(categoryDTO);
    }

}
