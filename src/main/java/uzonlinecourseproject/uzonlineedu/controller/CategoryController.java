package uzonlinecourseproject.uzonlineedu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uzonlinecourseproject.uzonlineedu.resAndreq.ApiResponse;
import uzonlinecourseproject.uzonlineedu.dto.CategoryDto;
import uzonlinecourseproject.uzonlineedu.exception.ResourceNotFoundException;
import uzonlinecourseproject.uzonlineedu.modify.CategoryCreateDto;
import uzonlinecourseproject.uzonlineedu.modify.CategoryUpdateDto;
import uzonlinecourseproject.uzonlineedu.service.category.CategoryServiceImpl;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryServiceImpl categoryService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id) {
        try {
            CategoryDto categoryDto = categoryService.getCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("Category found", categoryDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCategoryById(@PathVariable Long id) {
        try {
            categoryService.deleteCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("Category deleted successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse> getAllCategories(@PageableDefault(size = 10) Pageable pageable) {
        try {
            Page<CategoryDto> categoryPage = categoryService.getAllCategories(pageable);
            return ResponseEntity.ok(new ApiResponse("All categories fetched", categoryPage));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/count/categories")
    public ResponseEntity<ApiResponse> countCategories() {
        long count = categoryService.countAllCategories();
        if (count != 0) {
            return ResponseEntity.ok(new ApiResponse("All categories retrieved successfully", count));
        }
        return ResponseEntity.ok(new ApiResponse("All categories retrieved successfully", 0));
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<ApiResponse> existsById(@PathVariable Long id) {
        boolean exists = categoryService.existsById(id);
        return ResponseEntity.ok(new ApiResponse("Existence check completed", exists));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryUpdateDto categoryUpdateDto) {
        try {
            CategoryDto updatedCategory = categoryService.updateCategoryById(id, categoryUpdateDto);
            return ResponseEntity.ok(new ApiResponse("Category updated successfully", updatedCategory));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createCategory(@Valid @RequestBody CategoryCreateDto categoryCreateDto) {
        try {
            CategoryDto createdCategory = categoryService.createCategory(categoryCreateDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("Category created successfully", createdCategory));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }
}