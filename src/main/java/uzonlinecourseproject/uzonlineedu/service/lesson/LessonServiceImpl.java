package uzonlinecourseproject.uzonlineedu.service.lesson;

import org.springframework.data.domain.Pageable;
import uzonlinecourseproject.uzonlineedu.dto.LessonDto;
import uzonlinecourseproject.uzonlineedu.modify.LessonCreateDto;
import uzonlinecourseproject.uzonlineedu.modify.LessonUpdateDto;


import java.util.List;

public interface LessonServiceImpl {

    LessonDto getLessonById(Long id);

    void deleteLessonById(Long id);

    LessonDto updateLessonById(Long id, LessonUpdateDto lessonUpdateDto);

    LessonDto createLesson(LessonCreateDto lessonCreateDto);

    List<LessonDto> getAllLessons(Pageable pageable);

    List<LessonDto> getLessonsByCourseId(Long courseId, Pageable pageable);

    List<LessonDto> getLessonsByTitle(String title, Pageable pageable);

    boolean existsById(Long id);
    long  lessonCount ();
}
