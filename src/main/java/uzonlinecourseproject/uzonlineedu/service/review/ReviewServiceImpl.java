package uzonlinecourseproject.uzonlineedu.service.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uzonlinecourseproject.uzonlineedu.dto.ReviewDto;
import uzonlinecourseproject.uzonlineedu.modify.ReviewCreateDto;
import uzonlinecourseproject.uzonlineedu.modify.ReviewUpdateDto;


import java.util.List;

public interface ReviewServiceImpl {
    ReviewDto getReviewById(Long id);

    void deleteReviewById(Long id);

    ReviewDto updateReviewById(Long id, ReviewUpdateDto reviewUpdateDto);

    ReviewDto createReview(ReviewCreateDto reviewCreateDto);

    List<ReviewDto> getAllReviews();

    Page<ReviewDto> getAllReviews(Pageable pageable);

    List<ReviewDto> getReviewsByUserId(Long userId);

    Page<ReviewDto> getReviewsByUserId(Long userId, Pageable pageable);

    List<ReviewDto> getReviewsByCourseId(Long courseId);

    Page<ReviewDto> getReviewsByCourseId(Long courseId, Pageable pageable);

    List<ReviewDto> getRecentReviews(int limit);

    Double getAverageRatingByCourseId(Long courseId);

    boolean existsById(Long id);
}
