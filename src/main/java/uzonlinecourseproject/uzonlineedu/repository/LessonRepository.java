package uzonlinecourseproject.uzonlineedu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uzonlinecourseproject.uzonlineedu.entity.Lesson;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByCourseId(Long courseId, Pageable pageable);
    Page<Lesson> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
