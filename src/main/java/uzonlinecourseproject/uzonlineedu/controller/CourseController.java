package uzonlinecourseproject.uzonlineedu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uzonlinecourseproject.uzonlineedu.resAndreq.ApiResponse;
import uzonlinecourseproject.uzonlineedu.dto.CourseDto;
import uzonlinecourseproject.uzonlineedu.exception.ResourceNotFoundException;
import uzonlinecourseproject.uzonlineedu.modify.CourseCreateDto;
import uzonlinecourseproject.uzonlineedu.modify.CourseUpdateDto;
import uzonlinecourseproject.uzonlineedu.service.course.CourseService;

import java.util.List;

@RestController
@RequestMapping("/api/course")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createCourse(@RequestBody CourseCreateDto courseCreateDto) {
        try {
            CourseDto courseDto = courseService.createCourse(courseCreateDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("Course created successfully", courseDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateCourse(@PathVariable Long id, @RequestBody CourseUpdateDto courseUpdateDto) {
        try {
            CourseDto courseDto = courseService.updateCourseById(id, courseUpdateDto);
            return ResponseEntity.ok(new ApiResponse("Course updated successfully", courseDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteCourse(@PathVariable Long id) {
        try {
            courseService.deleteCourseById(id);
            return ResponseEntity.ok(new ApiResponse("Course deleted successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse> getCourseById(@PathVariable Long id) {
        try {
            CourseDto courseDto = courseService.getCourseById(id);
            return ResponseEntity.ok(new ApiResponse("Course retrieved successfully", courseDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse> getAllCourses(Pageable pageable) {
        List<CourseDto> courseDtos = courseService.getAllCourses(pageable);
        return ResponseEntity.ok(new ApiResponse("Courses retrieved successfully", courseDtos));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse> getCoursesByCategoryId(@PathVariable Long categoryId, Pageable pageable) {
        try {
            List<CourseDto> courseDtos = courseService.getCoursesByCategoryId(categoryId, pageable);
            return ResponseEntity.ok(new ApiResponse("Courses retrieved successfully", courseDtos));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }








    @GetMapping("/recent")
    public ResponseEntity<ApiResponse> getRecentCourses(@RequestParam(defaultValue = "10") int limit, Pageable pageable) {
        List<CourseDto> courseDtos = courseService.getRecentCourses(limit, pageable);
        return ResponseEntity.ok(new ApiResponse("Recent courses retrieved successfully", courseDtos));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> getCoursesByTitle(@RequestParam String title, Pageable pageable) {
        List<CourseDto> courseDtos = courseService.getCoursesByTitle(title, pageable);
        return ResponseEntity.ok(new ApiResponse("Courses retrieved successfully", courseDtos));
    }

    @GetMapping("/free-or-paid")
    public ResponseEntity<ApiResponse> getCoursesIsFreeIsPaid(@RequestParam boolean isFree, Pageable pageable) {
        List<CourseDto> courseDtos = courseService.getCoursesIsFreeIsPaid(isFree, pageable);
        return ResponseEntity.ok(new ApiResponse("Courses retrieved successfully", courseDtos));
    }


    @GetMapping("/exists/{id}")
    public ResponseEntity<ApiResponse> existsById(@PathVariable Long id) {
        boolean exists = courseService.existsById(id);
        return ResponseEntity.ok(new ApiResponse("Course existence checked", exists));
    }
    @GetMapping("/count/course")
    public ResponseEntity<ApiResponse> getTotalCourseCount() {
        long count = courseService.getAllCoursesCount();
        if (count != 0) {
            return ResponseEntity.ok(new ApiResponse("Total courses count retrieved successfully", count));
        }
        return ResponseEntity.ok(new ApiResponse("Total courses count retrieved successfully", 0));
    }
}