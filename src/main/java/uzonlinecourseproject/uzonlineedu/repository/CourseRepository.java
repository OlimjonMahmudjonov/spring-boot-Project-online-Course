package uzonlinecourseproject.uzonlineedu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uzonlinecourseproject.uzonlineedu.entity.Course;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByCategoryId(Long categoryId, Pageable pageable);
    Page<Course> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<Course> findByIsFree(boolean isFree, Pageable pageable);
}
