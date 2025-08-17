package uzonlinecourseproject.uzonlineedu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uzonlinecourseproject.uzonlineedu.resAndreq.ApiResponse;
import uzonlinecourseproject.uzonlineedu.dto.ReviewDto;
import uzonlinecourseproject.uzonlineedu.exception.ResourceNotFoundException;
import uzonlinecourseproject.uzonlineedu.modify.ReviewCreateDto;
import uzonlinecourseproject.uzonlineedu.modify.ReviewUpdateDto;
import uzonlinecourseproject.uzonlineedu.service.review.ReviewServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewServiceImpl reviewService;

    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse> getReviewById(@PathVariable Long id) {
        try {
            ReviewDto reviewDto = reviewService.getReviewById(id);
            return ResponseEntity.ok(new ApiResponse("Review found", reviewDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteReviewById(@PathVariable Long id) {
        try {
            reviewService.deleteReviewById(id);
            return ResponseEntity.ok(new ApiResponse("Review deleted successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse> getAllReviews() {
        List<ReviewDto> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(new ApiResponse("All reviews fetched", reviews));
    }

    @GetMapping("/paged")
    public ResponseEntity<ApiResponse> getAllReviewsPaged(@PageableDefault(size = 10) Pageable pageable) {
        Page<ReviewDto> reviewPage = reviewService.getAllReviews(pageable);
        return ResponseEntity.ok(new ApiResponse("All reviews fetched with pagination", reviewPage));
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<ApiResponse> existsById(@PathVariable Long id) {
        boolean exists = reviewService.existsById(id);
        return ResponseEntity.ok(new ApiResponse("Existence check completed", exists));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getReviewsByUserId(@PathVariable Long userId) {
        List<ReviewDto> reviews = reviewService.getReviewsByUserId(userId);
        return ResponseEntity.ok(new ApiResponse("Reviews fetched by user ID", reviews));
    }

    @GetMapping("/user/{userId}/paged")
    public ResponseEntity<ApiResponse> getReviewsByUserIdPaged(@PathVariable Long userId, @PageableDefault(size = 10) Pageable pageable) {
        Page<ReviewDto> reviewPage = reviewService.getReviewsByUserId(userId, pageable);
        return ResponseEntity.ok(new ApiResponse("Reviews fetched by user ID with pagination", reviewPage));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse> getReviewsByCourseId(@PathVariable Long courseId) {
        List<ReviewDto> reviews = reviewService.getReviewsByCourseId(courseId);
        return ResponseEntity.ok(new ApiResponse("Reviews fetched by course ID", reviews));
    }

    @GetMapping("/course/{courseId}/paged")
    public ResponseEntity<ApiResponse> getReviewsByCourseIdPaged(@PathVariable Long courseId, @PageableDefault(size = 10) Pageable pageable) {
        Page<ReviewDto> reviewPage = reviewService.getReviewsByCourseId(courseId, pageable);
        return ResponseEntity.ok(new ApiResponse("Reviews fetched by course ID with pagination", reviewPage));
    }

    @GetMapping("/recent")
    public ResponseEntity<ApiResponse> getRecentReviews(@RequestParam(defaultValue = "5") int limit) {
        List<ReviewDto> recentReviews = reviewService.getRecentReviews(limit);
        return ResponseEntity.ok(new ApiResponse("Recent reviews fetched", recentReviews));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateReview(
            @PathVariable Long id,
            @Valid @RequestBody ReviewUpdateDto reviewUpdateDto) {
        try {
            ReviewDto updatedReview = reviewService.updateReviewById(id, reviewUpdateDto);
            return ResponseEntity.ok(new ApiResponse("Review updated successfully", updatedReview));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/creat-review")
    public ResponseEntity<ApiResponse> createReview(@Valid @RequestBody ReviewCreateDto reviewCreateDto) {
        try {
            ReviewDto createdReview = reviewService.createReview(reviewCreateDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("Review created successfully", createdReview));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }
}
