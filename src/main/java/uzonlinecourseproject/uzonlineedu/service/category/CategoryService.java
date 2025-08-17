package uzonlinecourseproject.uzonlineedu.service.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uzonlinecourseproject.uzonlineedu.dto.CategoryDto;
import uzonlinecourseproject.uzonlineedu.entity.Category;
import uzonlinecourseproject.uzonlineedu.entity.Course;
import uzonlinecourseproject.uzonlineedu.exception.ResourceNotFoundException;
import uzonlinecourseproject.uzonlineedu.modify.CategoryCreateDto;
import uzonlinecourseproject.uzonlineedu.modify.CategoryUpdateDto;
import uzonlinecourseproject.uzonlineedu.repository.CategoryRepository;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class CategoryService implements CategoryServiceImpl {
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + id));
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.findById(id).ifPresentOrElse(categoryRepository::delete,
                () -> {
                    throw new ResourceNotFoundException("Category not found with id " + id);
                });
    }

    @Override
    public CategoryDto updateCategoryById(Long id, CategoryUpdateDto categoryUpdateDto) {
        return categoryRepository.findById(id)
                .map(existingCategory -> {
                    if (categoryUpdateDto.getName() != null) {
                        existingCategory.setName(categoryUpdateDto.getName());
                    }
                    if (categoryUpdateDto.getDescription() != null) {
                        existingCategory.setDescription(categoryUpdateDto.getDescription());
                    }
                    return convertToDto(categoryRepository.save(existingCategory));
                }).orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + id));
    }

    @Override
    public CategoryDto createCategory(CategoryCreateDto categoryCreateDto) {
        Category category = new Category();
        category.setName(categoryCreateDto.getName());
        category.setDescription(categoryCreateDto.getDescription());
        return convertToDto(categoryRepository.save(category));
    }

    @Override
    public Page<CategoryDto> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(this::convertToDto);
    }

    @Override
    public Page<CategoryDto> searchCategoriesByName(Pageable pageable, String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            return getAllCategories(pageable);
        }
        return categoryRepository.findByNameContainingIgnoreCase(searchText, pageable)
                .map(this::convertToDto);
    }

    @Override
    public boolean existsById(Long id) {
        return categoryRepository.existsById(id);
    }

    @Override
    public long countAllCategories() { // TODO  fix counter
        return 0;
    }

    @Override
    public long countCourses() {//TODO fix
        return  categoryRepository.count();
    }

    private CategoryDto convertToDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setDescription(category.getDescription());
        if (category.getCourses() != null) {
            categoryDto.setCourseIds(category.getCourses().stream()
                    .map(Course::getId)
                    .collect(Collectors.toList()));
        }
        return categoryDto;
    }
}