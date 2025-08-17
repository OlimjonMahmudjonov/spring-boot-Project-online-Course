package uzonlinecourseproject.uzonlineedu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uzonlinecourseproject.uzonlineedu.entity.CourseComment;

public interface CourseCommentRepository extends JpaRepository<CourseComment, Long> {

    Page<CourseComment> findByUserId(Long userId, Pageable pageable);

    Page<CourseComment> findByCourseId(Long courseId, Pageable pageable);
}
