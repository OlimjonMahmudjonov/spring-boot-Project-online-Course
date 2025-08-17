package uzonlinecourseproject.uzonlineedu.service.course;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uzonlinecourseproject.uzonlineedu.dto.CourseCommentDto;
import uzonlinecourseproject.uzonlineedu.dto.CourseDto;
import uzonlinecourseproject.uzonlineedu.dto.LessonDto;
import uzonlinecourseproject.uzonlineedu.dto.ReviewDto;
import uzonlinecourseproject.uzonlineedu.entity.*;
import uzonlinecourseproject.uzonlineedu.exception.ResourceNotFoundException;
import uzonlinecourseproject.uzonlineedu.modify.CourseCreateDto;
import uzonlinecourseproject.uzonlineedu.modify.CourseUpdateDto;
import uzonlinecourseproject.uzonlineedu.repository.CategoryRepository;
import uzonlinecourseproject.uzonlineedu.repository.CourseRepository;
import uzonlinecourseproject.uzonlineedu.repository.LessonRepository;
import uzonlinecourseproject.uzonlineedu.repository.VideoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class CourseService implements CourseServiceImpl {
    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final LessonRepository lessonRepository;
    private final VideoRepository videoRepository;

    @Override
    public CourseDto getCourseById(Long id) {
        return courseRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
    }

    @Override
    public void deleteCourseById(Long id) {
        courseRepository.findById(id).ifPresentOrElse(courseRepository::delete,
                () -> {
                    throw new ResourceNotFoundException("Course not found");
                });
    }

    @Override
    public CourseDto updateCourseById(Long id, CourseUpdateDto courseUpdateDto) {
        return courseRepository.findById(id)
                .map(existingCourse -> {
                    if (courseUpdateDto.getTitle() != null) {
                        existingCourse.setTitle(courseUpdateDto.getTitle());
                    }
                    if (courseUpdateDto.getDescription() != null) {
                        existingCourse.setDescription(courseUpdateDto.getDescription());
                    }
                    if (courseUpdateDto.getOriginalPrice() != null) {
                        existingCourse.setOriginalPrice(courseUpdateDto.getOriginalPrice());
                    }
                    if (courseUpdateDto.getDiscountPrice() != null) {
                        existingCourse.setDiscountPrice(courseUpdateDto.getDiscountPrice());
                    }
                    if (courseUpdateDto.getDiscountEndDate() != null) {
                        existingCourse.setDiscountEndDate(courseUpdateDto.getDiscountEndDate());
                    }
                    if (courseUpdateDto.getIsFree() != null) {
                        existingCourse.setIsFree(courseUpdateDto.getIsFree());
                    }
                    if (courseUpdateDto.getDuration() != null) {
                        existingCourse.setDuration(courseUpdateDto.getDuration());
                    }
                    if (courseUpdateDto.getLevel() != null) {
                        existingCourse.setLevel(courseUpdateDto.getLevel());
                    }
                    if (courseUpdateDto.getCategoryId() != null) {
                        Category category = categoryRepository.findById(courseUpdateDto.getCategoryId())
                                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + courseUpdateDto.getCategoryId()));
                        existingCourse.setCategory(category);
                    }
                    if (courseUpdateDto.getLessonIds() != null) {
                        List<Lesson> lessons = courseUpdateDto.getLessonIds().stream()
                                .map(lessonId -> lessonRepository.findById(lessonId)
                                        .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id " + lessonId)))
                                .collect(Collectors.toList());
                        existingCourse.setLessons(lessons);
                    }
                    if (courseUpdateDto.getPreviewVideoId() != null) {
                        Video video = videoRepository.findById(courseUpdateDto.getPreviewVideoId())
                                .orElseThrow(() -> new ResourceNotFoundException("Video not found with id " + courseUpdateDto.getPreviewVideoId()));
                        existingCourse.setPreviewVideo(video);
                    }
                    return convertToDto(courseRepository.save(existingCourse));
                }).orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + id));
    }

    @Override
    public CourseDto createCourse(CourseCreateDto courseCreateDto) {
        Category category = categoryRepository.findById(courseCreateDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + courseCreateDto.getCategoryId()));

        return Optional.of(courseCreateDto)
                .map(req -> {
                    Course course = new Course();
                    course.setTitle(req.getTitle());
                    course.setDescription(req.getDescription());
                    course.setOriginalPrice(req.getOriginalPrice());
                    course.setDiscountPrice(req.getDiscountPrice());
                    course.setDiscountEndDate(req.getDiscountEndDate());
                    course.setIsFree(req.getIsFree());
                    course.setDuration(req.getDuration());
                    course.setLevel(req.getLevel());
                    course.setCategory(category);
                    if (req.getLessonIds() != null) {
                        List<Lesson> lessons = req.getLessonIds().stream()
                                .map(lessonId -> lessonRepository.findById(lessonId)
                                        .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id " + lessonId)))
                                .collect(Collectors.toList());
                        course.setLessons(lessons);
                    }
                    if (req.getPreviewVideoId() != null) {
                        Video video = videoRepository.findById(req.getPreviewVideoId())
                                .orElseThrow(() -> new ResourceNotFoundException("Video not found with id " + req.getPreviewVideoId()));
                        course.setPreviewVideo(video);
                    }
                    return convertToDto(courseRepository.save(course));
                }).orElseThrow(() -> new ResourceNotFoundException("Invalid course data"));
    }

    @Override
    public List<CourseDto> getAllCourses(Pageable pageable) {
        return courseRepository.findAll(pageable)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseDto> getCoursesByCategoryId(Long categoryId, Pageable pageable) {
        categoryRepository.findById(categoryId).orElseThrow(() ->
                new ResourceNotFoundException("Category not found with id " + categoryId));

        return courseRepository.findByCategoryId(categoryId, pageable)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseDto> getRecentCourses(int limit, Pageable pageable) {
        return courseRepository.findAll(pageable)
                .stream()
                .sorted((courseOne, courseTwo) -> {
                    LocalDateTime timeOne = courseOne.getDiscountEndDate() != null ? courseOne.getDiscountEndDate() : LocalDateTime.MIN;
                    LocalDateTime timeTwo = courseTwo.getDiscountEndDate() != null ? courseTwo.getDiscountEndDate() : LocalDateTime.MIN;
                    return timeTwo.compareTo(timeOne);
                })
                .limit(limit)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseDto> getCoursesByTitle(String title, Pageable pageable) {
        if (title == null || title.trim().isEmpty()) {
            return getAllCourses(pageable);
        }
        return courseRepository.findByTitleContainingIgnoreCase(title, pageable)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseDto> getCoursesIsFreeIsPaid(boolean isFree, Pageable pageable) {
        return courseRepository.findByIsFree(isFree, pageable)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(Long id) {
        return courseRepository.existsById(id);
    }

    @Override
    public long getAllCoursesCount() {
        return courseRepository.count();
    }

    private CourseDto convertToDto(Course course) {
        CourseDto courseDto = new CourseDto();
        courseDto.setId(course.getId());
        courseDto.setTitle(course.getTitle());
        courseDto.setDescription(course.getDescription());
        courseDto.setOriginalPrice(course.getOriginalPrice());
        courseDto.setDiscountPrice(course.getDiscountPrice());
        courseDto.setDiscountEndDate(course.getDiscountEndDate());
        courseDto.setIsFree(course.getIsFree());
        courseDto.setDuration(course.getDuration());
        courseDto.setLevel(course.getLevel());
        if (course.getCategory() != null) {
            courseDto.setCategoryId(course.getCategory().getId());
        }
        if (course.getLessons() != null) {
            courseDto.setLessons(course.getLessons().stream()
                    .map(lesson -> {
                        LessonDto lessonDto = new LessonDto();
                        lessonDto.setId(lesson.getId());
                        lessonDto.setTitle(lesson.getTitle());
                        lessonDto.setContent(lesson.getContent());
                        lessonDto.setVideoUrl(lesson.getVideoUrl());
                        lessonDto.setLessonOrder(lesson.getLessonOrder());
                        lessonDto.setCourseId(lesson.getCourse() != null ? lesson.getCourse().getId() : null);
                        lessonDto.setVideoId(lesson.getVideo() != null ? lesson.getVideo().getId() : null);
                        return lessonDto;
                    })
                    .collect(Collectors.toList()));
        }
        if (course.getReviews() != null) {
            courseDto.setReviews(course.getReviews().stream()
                    .map(review -> {
                        ReviewDto reviewDto = new ReviewDto();
                        reviewDto.setId(review.getId());
                        reviewDto.setRating(review.getRating());
                        reviewDto.setComment(review.getComment());
                        reviewDto.setCreatedAt(review.getCreatedAt());
                        reviewDto.setUserId(review.getUser() != null ? review.getUser().getId() : null);
                        reviewDto.setCourseId(review.getCourse() != null ? review.getCourse().getId() : null);
                        return reviewDto;
                    })
                    .collect(Collectors.toList()));
        }
        if (course.getComments() != null) {
            courseDto.setComments(course.getComments().stream()
                    .map(comment -> {
                        CourseCommentDto commentDto = new CourseCommentDto();
                        commentDto.setId(comment.getId());
                        commentDto.setContent(comment.getContent());
                        commentDto.setCreatedAt(comment.getCreatedAt());
                        commentDto.setUserId(comment.getUser() != null ? comment.getUser().getId() : null);
                        commentDto.setCourseId(comment.getCourse() != null ? comment.getCourse().getId() : null);
                        return commentDto;
                    })
                    .collect(Collectors.toList()));
        }
        if (course.getEnrolledUsers() != null) {
            courseDto.setEnrolledUserIds(course.getEnrolledUsers().stream()
                    .map(User::getId)
                    .collect(Collectors.toList()));
        }
        if (course.getPreviewVideo() != null) {
            courseDto.setPreviewVideoId(course.getPreviewVideo().getId());
        }
        return courseDto;
    }
}