package uzonlinecourseproject.uzonlineedu.service.courseComent;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uzonlinecourseproject.uzonlineedu.dto.CourseCommentDto;
import uzonlinecourseproject.uzonlineedu.modify.CourseCommentCreateDto;
import uzonlinecourseproject.uzonlineedu.modify.CourseCommentUpdateDto;


import java.util.List;

public interface CourseCommentServiceImpl {
    CourseCommentDto getCourseCommentById(Long id);

    void deleteCourseCommentById(Long id);

    CourseCommentDto updateCourseCommentById(Long id, CourseCommentUpdateDto courseCommentUpdateDto);

    CourseCommentDto createCourseComment(CourseCommentCreateDto courseCommentCreateDto);

    List<CourseCommentDto> getAllCourseComments();


    Page<CourseCommentDto> getAllCourseComments(Pageable pageable);

    List<CourseCommentDto> getCourseCommentsByUserId(Long userId);


    Page<CourseCommentDto> getCourseCommentsByUserId(Long userId, Pageable pageable);

    List<CourseCommentDto> getCourseCommentsByCourseId(Long courseId);


    Page<CourseCommentDto> getCourseCommentsByCourseId(Long courseId, Pageable pageable);

    List<CourseCommentDto> getRecentCourseComments(int limit);

    boolean existsById(Long id);
}
