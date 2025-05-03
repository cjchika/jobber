package com.cjchika.jobber.category.controller;

import com.cjchika.jobber.api.ApiResponse;
import com.cjchika.jobber.category.dto.CategoryRequestDTO;
import com.cjchika.jobber.category.dto.CategoryResponseDTO;
import com.cjchika.jobber.category.dto.CategoryUpdateDTO;
import com.cjchika.jobber.category.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category", description = "Endpoints for job categories")
public class CategoryController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping(produces = "application/json")
    @Operation(summary = "Get all categories", description = "Retrieve all job categories")
    public ResponseEntity<ApiResponse<List<CategoryResponseDTO>>> getAllCategories() {
        try {
            logger.info("Fetching all categories");
            List<CategoryResponseDTO> categories = categoryService.getAllCategories();
            return ApiResponse.success(categories, "Categories retrieved successfully", HttpStatus.OK);
        } catch (Exception ex){
            logger.error("Error fetching categories: {}", ex.getMessage());
            return ApiResponse.error(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(produces = "application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Create category", description = "Create a new job category (Admin only)")
    public ResponseEntity<ApiResponse<CategoryResponseDTO>> createCategory(@Valid @RequestBody CategoryRequestDTO categoryRequestDTO){
        try {
            logger.info("Creating new category");
            CategoryResponseDTO category = categoryService.createCategory(categoryRequestDTO);
            return ApiResponse.success(category, "Category created successfully", HttpStatus.CREATED);
        } catch (Exception ex){
            logger.error("Error creating category: {}", ex.getMessage());
            return ApiResponse.error(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    @Operation(summary = "Get category by ID", description = "Retrieve a specific category by its ID")
    public ResponseEntity<ApiResponse<CategoryResponseDTO>> getCategoryById(@PathVariable UUID id) {
        try {
            logger.info("Fetching category with ID: {}", id);
            CategoryResponseDTO  category = categoryService.getCategoryById(id);
            return ApiResponse.success(category, "Category retrieved successfully", HttpStatus.OK);
        } catch (Exception ex){
            logger.error("Error fetching category: {}", ex.getMessage());
            return ApiResponse.error(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping(value = "/{id}", produces = "application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Update category", description = "Update an existing category (Admin only)")
    public ResponseEntity<ApiResponse<CategoryResponseDTO>> updateCategory(@PathVariable UUID id, @Valid @RequestBody CategoryUpdateDTO categoryUpdateDTO){
        try {
            logger.info("Updating category with ID: {}", id);
            CategoryResponseDTO updateCategory = categoryService.updateCategory(id, categoryUpdateDTO);
            return ApiResponse.success(updateCategory, "Category updated successfully", HttpStatus.OK);
        } catch (Exception ex){
            logger.error("Error updating category: {}", ex.getMessage());
            return ApiResponse.error(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete category", description = "Delete a category (Admin only)")
    public void deleteCategory(@PathVariable UUID id) {
        logger.info("Deleting category with ID: {}", id);
        categoryService.deleteCategory(id);
    }
}
