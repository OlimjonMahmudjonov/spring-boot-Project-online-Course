package uzonlinecourseproject.uzonlineedu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uzonlinecourseproject.uzonlineedu.entity.Blog;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    Page<Blog> findByAuthorId(Long authorId, Pageable pageable);
}
