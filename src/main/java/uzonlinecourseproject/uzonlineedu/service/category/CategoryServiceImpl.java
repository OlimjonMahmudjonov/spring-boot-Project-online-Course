package uzonlinecourseproject.uzonlineedu.service.category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uzonlinecourseproject.uzonlineedu.dto.CategoryDto;
import uzonlinecourseproject.uzonlineedu.modify.CategoryCreateDto;
import uzonlinecourseproject.uzonlineedu.modify.CategoryUpdateDto;

public interface CategoryServiceImpl {

    CategoryDto getCategoryById(Long id);

    void deleteCategoryById(Long id);

    CategoryDto updateCategoryById(Long id, CategoryUpdateDto categoryUpdateDto);

    CategoryDto createCategory(CategoryCreateDto categoryCreateDto);

    Page<CategoryDto> getAllCategories(Pageable pageable);

    Page<CategoryDto> searchCategoriesByName(Pageable pageable, String searchText);

    boolean existsById(Long id);

    long countAllCategories();
    long countCourses ();
}