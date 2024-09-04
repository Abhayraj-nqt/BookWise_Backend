package com.bookwise.bookwise.controller;

import com.bookwise.bookwise.constants.CategoryConstants;
import com.bookwise.bookwise.dto.category.CategoryDTO;
import com.bookwise.bookwise.dto.response.ResponseDTO;
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
            Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(sortDir), sortBy);
            Page<CategoryDTO> categoryDTOPage = iCategoryService.getCategories(pageable, search);
            return ResponseEntity.status(HttpStatus.OK).body(categoryDTOPage);
        }
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable Long id) {
        CategoryDTO categoryDTO = iCategoryService.getCategoryById(id);
        return ResponseEntity.status(HttpStatus.OK).body(categoryDTO);
    }

    @PostMapping("/category")
    public ResponseEntity<ResponseDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        CategoryDTO savedCategoryDTO = iCategoryService.createCategory(categoryDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(HttpStatus.OK.toString(), CategoryConstants.CATEGORY_CREATE_MSG));
    }

    @PutMapping("/category/{id}")
    public ResponseEntity<ResponseDTO> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO updatedCategoryDTO = iCategoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(HttpStatus.OK.toString(), CategoryConstants.CATEGORY_UPDATE_MSG));
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<ResponseDTO> deleteCategory(@PathVariable Long id) {
        CategoryDTO categoryDTO = iCategoryService.deleteCategoryById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(HttpStatus.OK.toString(), CategoryConstants.CATEGORY_DELETE_MSG));
    }

}
