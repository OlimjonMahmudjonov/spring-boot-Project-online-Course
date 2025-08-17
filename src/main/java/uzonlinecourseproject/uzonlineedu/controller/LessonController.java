package uzonlinecourseproject.uzonlineedu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uzonlinecourseproject.uzonlineedu.resAndreq.ApiResponse;
import uzonlinecourseproject.uzonlineedu.dto.LessonDto;
import uzonlinecourseproject.uzonlineedu.exception.ResourceNotFoundException;
import uzonlinecourseproject.uzonlineedu.modify.LessonCreateDto;
import uzonlinecourseproject.uzonlineedu.modify.LessonUpdateDto;
import uzonlinecourseproject.uzonlineedu.service.lesson.LessonService;

import java.util.List;

@RestController
@RequestMapping("/api/lesson")
@RequiredArgsConstructor
public class LessonController {
    private final LessonService lessonService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createLesson(@RequestBody LessonCreateDto lessonCreateDto) {
        try {
            LessonDto lessonDto = lessonService.createLesson(lessonCreateDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("Lesson created successfully", lessonDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateLesson(@PathVariable Long id, @RequestBody LessonUpdateDto lessonUpdateDto) {
        try {
            LessonDto lessonDto = lessonService.updateLessonById(id, lessonUpdateDto);
            return ResponseEntity.ok(new ApiResponse("Lesson updated successfully", lessonDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteLesson(@PathVariable Long id) {
        try {
            lessonService.deleteLessonById(id);
            return ResponseEntity.ok(new ApiResponse("Lesson deleted successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getLessonById(@PathVariable Long id) {
        try {
            LessonDto lessonDto = lessonService.getLessonById(id);
            return ResponseEntity.ok(new ApiResponse("Lesson retrieved successfully", lessonDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse> getAllLessons(Pageable pageable) {
        List<LessonDto> lessonDtos = lessonService.getAllLessons(pageable);
        return ResponseEntity.ok(new ApiResponse("Lessons retrieved successfully", lessonDtos));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse> getLessonsByCourseId(@PathVariable Long courseId, Pageable pageable) {
        try {
            List<LessonDto> lessonDtos = lessonService.getLessonsByCourseId(courseId, pageable);
            return ResponseEntity.ok(new ApiResponse("Lessons retrieved successfully", lessonDtos));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> getLessonsByTitle(@RequestParam String title, Pageable pageable) {
        List<LessonDto> lessonDtos = lessonService.getLessonsByTitle(title, pageable);
        return ResponseEntity.ok(new ApiResponse("Lessons retrieved successfully", lessonDtos));
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<ApiResponse> existsById(@PathVariable Long id) {
        boolean exists = lessonService.existsById(id);
        return ResponseEntity.ok(new ApiResponse("Lesson existence checked", exists));
    }

    @GetMapping("/count/lesson")
    public ResponseEntity<ApiResponse> countLessons() {
        long count = lessonService.lessonCount();
        if (count != 0) {
            return ResponseEntity.ok(new ApiResponse("Lesson count retrieved successfully", count));
        }
        return ResponseEntity.ok(new ApiResponse("Lesson count retrieved successfully", 0));
    }
}