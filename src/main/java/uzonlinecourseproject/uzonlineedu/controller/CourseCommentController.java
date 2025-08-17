package uzonlinecourseproject.uzonlineedu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uzonlinecourseproject.uzonlineedu.resAndreq.ApiResponse;
import uzonlinecourseproject.uzonlineedu.dto.CourseCommentDto;
import uzonlinecourseproject.uzonlineedu.exception.ResourceNotFoundException;
import uzonlinecourseproject.uzonlineedu.modify.CourseCommentCreateDto;
import uzonlinecourseproject.uzonlineedu.modify.CourseCommentUpdateDto;
import uzonlinecourseproject.uzonlineedu.service.courseComent.CourseCommentService;

import java.util.List;

@RestController
@RequestMapping("/api/course-comment")
@RequiredArgsConstructor
public class CourseCommentController {
    private final CourseCommentService courseCommentService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createCourseComment(@RequestBody CourseCommentCreateDto courseCommentCreateDto) {
        try {
            CourseCommentDto commentDto = courseCommentService.createCourseComment(courseCommentCreateDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("Course comment created successfully", commentDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateCourseComment(@PathVariable Long id, @RequestBody CourseCommentUpdateDto courseCommentUpdateDto) {
        try {
            CourseCommentDto commentDto = courseCommentService.updateCourseCommentById(id, courseCommentUpdateDto);
            return ResponseEntity.ok(new ApiResponse("Course comment updated successfully", commentDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCourseComment(@PathVariable Long id) {
        try {
            courseCommentService.deleteCourseCommentById(id);
            return ResponseEntity.ok(new ApiResponse("Course comment deleted successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getCourseCommentById(@PathVariable Long id) {
        try {
            CourseCommentDto commentDto = courseCommentService.getCourseCommentById(id);
            return ResponseEntity.ok(new ApiResponse("Course comment retrieved successfully", commentDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse> getAllCourseComments(Pageable pageable) {
        Page<CourseCommentDto> commentPage = courseCommentService.getAllCourseComments(pageable);
        return ResponseEntity.ok(new ApiResponse("Course comments retrieved successfully", commentPage.getContent()));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getCourseCommentsByUserId(@PathVariable Long userId, Pageable pageable) {
        try {
            Page<CourseCommentDto> commentPage = courseCommentService.getCourseCommentsByUserId(userId, pageable);
            return ResponseEntity.ok(new ApiResponse("Course comments retrieved successfully", commentPage.getContent()));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse> getCourseCommentsByCourseId(@PathVariable Long courseId, Pageable pageable) {
        try {
            Page<CourseCommentDto> commentPage = courseCommentService.getCourseCommentsByCourseId(courseId, pageable);
            return ResponseEntity.ok(new ApiResponse("Course comments retrieved successfully", commentPage.getContent()));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/recent")
    public ResponseEntity<ApiResponse> getRecentCourseComments(@RequestParam(defaultValue = "10") int limit) {
        List<CourseCommentDto> commentDtos = courseCommentService.getRecentCourseComments(limit);
        return ResponseEntity.ok(new ApiResponse("Recent course comments retrieved successfully", commentDtos));
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<ApiResponse> existsById(@PathVariable Long id) {
        boolean exists = courseCommentService.existsById(id);
        return ResponseEntity.ok(new ApiResponse("Course comment existence checked", exists));
    }
}