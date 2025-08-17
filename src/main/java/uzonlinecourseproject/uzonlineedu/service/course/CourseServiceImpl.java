package uzonlinecourseproject.uzonlineedu.service.course;

import org.springframework.data.domain.Pageable;
import uzonlinecourseproject.uzonlineedu.dto.CourseDto;
import uzonlinecourseproject.uzonlineedu.entity.Course;
import uzonlinecourseproject.uzonlineedu.modify.CourseCreateDto;
import uzonlinecourseproject.uzonlineedu.modify.CourseUpdateDto;

import java.util.List;

public interface CourseServiceImpl {

    CourseDto getCourseById(Long id);

    void deleteCourseById(Long id);

    CourseDto updateCourseById(Long id, CourseUpdateDto courseUpdateDto);

    CourseDto createCourse(CourseCreateDto courseCreateDto);

    List<CourseDto> getAllCourses(Pageable pageable);

    List<CourseDto> getCoursesByCategoryId(Long categoryId, Pageable pageable);

    List<CourseDto> getRecentCourses(int limit, Pageable pageable);

    List<CourseDto> getCoursesByTitle(String title, Pageable pageable);

    List<CourseDto> getCoursesIsFreeIsPaid( boolean isFree , Pageable pageable);

    boolean existsById(Long id);

    long getAllCoursesCount();
}
