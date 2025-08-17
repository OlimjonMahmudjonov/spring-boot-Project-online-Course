package uzonlinecourseproject.uzonlineedu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uzonlinecourseproject.uzonlineedu.entity.Video;

public interface VideoRepository  extends JpaRepository<Video, Long> {
    Page<Video> findAll(Pageable pageable);

    Page<Video> findByCoursesId(Long courseId, Pageable pageable);

    Page<Video> findByLessonsId(Long lessonId, Pageable pageable);
    Page<Video> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
