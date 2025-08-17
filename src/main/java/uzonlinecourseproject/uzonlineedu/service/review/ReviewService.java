package uzonlinecourseproject.uzonlineedu.service.review;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uzonlinecourseproject.uzonlineedu.dto.ReviewDto;
import uzonlinecourseproject.uzonlineedu.entity.Course;
import uzonlinecourseproject.uzonlineedu.entity.Review;
import uzonlinecourseproject.uzonlineedu.entity.User;
import uzonlinecourseproject.uzonlineedu.exception.ResourceNotFoundException;
import uzonlinecourseproject.uzonlineedu.modify.ReviewCreateDto;
import uzonlinecourseproject.uzonlineedu.modify.ReviewUpdateDto;
import uzonlinecourseproject.uzonlineedu.repository.CourseRepository;
import uzonlinecourseproject.uzonlineedu.repository.ReviewRepository;
import uzonlinecourseproject.uzonlineedu.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class ReviewService implements ReviewServiceImpl {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Override
    public ReviewDto getReviewById(Long id) {
        return reviewRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
    }

    @Override
    public void deleteReviewById(Long id) {
        reviewRepository.findById(id).ifPresentOrElse(reviewRepository::delete,
                () -> { throw new ResourceNotFoundException("Review not found"); });
    }

    @Override
    public ReviewDto updateReviewById(Long id, ReviewUpdateDto reviewUpdateDto) {
        return reviewRepository.findById(id)
                .map(existingReview -> {
                    if (reviewUpdateDto.getRating() != null) {
                        existingReview.setRating(reviewUpdateDto.getRating());
                    }
                    if (reviewUpdateDto.getComment() != null) {
                        existingReview.setComment(reviewUpdateDto.getComment());
                    }
                    return convertToDto(reviewRepository.save(existingReview));
                }).orElseThrow(() -> new ResourceNotFoundException("Review not found with id " + id));
    }

    @Override
    public ReviewDto createReview(ReviewCreateDto reviewCreateDto) {
        User user = userRepository.findById(reviewCreateDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + reviewCreateDto.getUserId()));
        Course course = courseRepository.findById(reviewCreateDto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + reviewCreateDto.getCourseId()));

        return Optional.of(reviewCreateDto)
                .map(req -> {
                    Review review = new Review();
                    review.setRating(req.getRating());
                    review.setComment(req.getComment());
                    review.setCreatedAt(LocalDateTime.now());
                    review.setUser(user);
                    review.setCourse(course);
                    return convertToDto(reviewRepository.save(review));
                }).orElseThrow(() -> new ResourceNotFoundException("Invalid review data"));
    }

    @Override
    public List<ReviewDto> getAllReviews() {
        return reviewRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ReviewDto> getAllReviews(Pageable pageable) {
        return reviewRepository.findAll(pageable).map(this::convertToDto);
    }

    @Override
    public List<ReviewDto> getReviewsByUserId(Long userId) {
        userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User not found with id " + userId));

        return reviewRepository.findAll().stream()
                .filter(review -> review.getUser() != null && review.getUser().getId().equals(userId))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ReviewDto> getReviewsByUserId(Long userId, Pageable pageable) {
        userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User not found with id " + userId));
        return reviewRepository.findByUserId(userId, pageable).map(this::convertToDto);
    }

    @Override
    public List<ReviewDto> getReviewsByCourseId(Long courseId) {
        courseRepository.findById(courseId).orElseThrow(() ->
                new ResourceNotFoundException("Course not found with id " + courseId));

        return reviewRepository.findAll().stream()
                .filter(review -> review.getCourse() != null && review.getCourse().getId().equals(courseId))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ReviewDto> getReviewsByCourseId(Long courseId, Pageable pageable) {
        courseRepository.findById(courseId).orElseThrow(() ->
                new ResourceNotFoundException("Course not found with id " + courseId));
        return reviewRepository.findByCourseId(courseId, pageable).map(this::convertToDto);
    }

    @Override
    public List<ReviewDto> getRecentReviews(int limit) {
        return reviewRepository.findAll()
                .stream()
                .sorted((r1, r2) -> r2.getCreatedAt().compareTo(r1.getCreatedAt()))
                .limit(limit)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Double getAverageRatingByCourseId(Long courseId) {
        courseRepository.findById(courseId).orElseThrow(() ->
                new ResourceNotFoundException("Course not found with id " + courseId));

        Page<Review> reviews = reviewRepository.findByCourseId(courseId, Pageable.unpaged());

        if (reviews.isEmpty()) {
            return 0.0;
        }

        double sum = reviews.getContent().stream().mapToDouble(Review::getRating).sum();
        return sum / reviews.getNumberOfElements();
    }

    @Override
    public boolean existsById(Long id) {
        return reviewRepository.existsById(id);
    }

    private ReviewDto convertToDto(Review review) {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setId(review.getId());
        reviewDto.setRating(review.getRating());
        reviewDto.setComment(review.getComment());
        reviewDto.setCreatedAt(review.getCreatedAt());
        if (review.getUser() != null) {
            reviewDto.setUserId(review.getUser().getId());
        }
        if (review.getCourse() != null) {
            reviewDto.setCourseId(review.getCourse().getId());
        }
        return reviewDto;
    }
}