package uzonlinecourseproject.uzonlineedu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uzonlinecourseproject.uzonlineedu.resAndreq.ApiResponse;
import uzonlinecourseproject.uzonlineedu.dto.QuestionAnswerDto;
import uzonlinecourseproject.uzonlineedu.exception.ResourceNotFoundException;
import uzonlinecourseproject.uzonlineedu.modify.QuestionAnswerCreateDto;
import uzonlinecourseproject.uzonlineedu.modify.QuestionAnswerUpdateDto;
import uzonlinecourseproject.uzonlineedu.service.questionAnswer.IQuestionAnswerService;

import java.util.List;

@RestController
@RequestMapping("/api/question-answers")
@RequiredArgsConstructor
public class QuestionAnswerController {

    private final IQuestionAnswerService questionAnswerService;

    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse> getQuestionAnswerById(@PathVariable Long id) {
        try {
            QuestionAnswerDto dto = questionAnswerService.getQuestionAnswerById(id);
            return ResponseEntity.ok(new ApiResponse("QuestionAnswer found", dto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteQuestionAnswerById(@PathVariable Long id) {
        try {
            questionAnswerService.deleteQuestionAnswerById(id);
            return ResponseEntity.ok(new ApiResponse("QuestionAnswer deleted successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse> getAllQuestionAnswers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<QuestionAnswerDto> list = questionAnswerService.getAllQuestionAnswers(pageable);
        return ResponseEntity.ok(new ApiResponse("All QuestionAnswers fetched", list));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getQuestionAnswersByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        try {
            List<QuestionAnswerDto> list = questionAnswerService.getQuestionAnswersByUserId(userId, pageable);
            return ResponseEntity.ok(new ApiResponse("QuestionAnswers fetched by user ID", list));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse> getQuestionAnswersByCourseId(
            @PathVariable Long courseId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        try {
            List<QuestionAnswerDto> list = questionAnswerService.getQuestionAnswersByCourseId(courseId, pageable);
            return ResponseEntity.ok(new ApiResponse("QuestionAnswers fetched by course ID", list));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/recent")
    public ResponseEntity<ApiResponse> getRecentQuestionAnswers(
            @RequestParam(defaultValue = "5") int limit,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<QuestionAnswerDto> list = questionAnswerService.getRecentQuestionAnswers(limit, pageable);
        return ResponseEntity.ok(new ApiResponse("Recent QuestionAnswers fetched", list));
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<ApiResponse> existsById(@PathVariable Long id) {
        boolean exists = questionAnswerService.existsById(id);
        return ResponseEntity.ok(new ApiResponse("Existence check completed", exists));
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createQuestionAnswer(
            @Valid @RequestBody QuestionAnswerCreateDto createDto) {
        try {
            QuestionAnswerDto dto = questionAnswerService.createQuestionAnswer(createDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("QuestionAnswer created successfully", dto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateQuestionAnswer(
            @PathVariable Long id,
            @Valid @RequestBody QuestionAnswerUpdateDto updateDto) {
        try {
            QuestionAnswerDto dto = questionAnswerService.updateQuestionAnswerById(id, updateDto);
            return ResponseEntity.ok(new ApiResponse("QuestionAnswer updated successfully", dto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
