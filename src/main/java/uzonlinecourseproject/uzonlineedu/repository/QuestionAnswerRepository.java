package uzonlinecourseproject.uzonlineedu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uzonlinecourseproject.uzonlineedu.entity.QuestionAnswer;

public interface QuestionAnswerRepository  extends JpaRepository<QuestionAnswer, Long> {

    Page<QuestionAnswer> findByUserId(Long userId, Pageable pageable);

    Page<QuestionAnswer> findByCourseId(Long courseId, Pageable pageable);
}
