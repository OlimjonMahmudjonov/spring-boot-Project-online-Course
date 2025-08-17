package uzonlinecourseproject.uzonlineedu.service.courseComent;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uzonlinecourseproject.uzonlineedu.dto.CourseCommentDto;
import uzonlinecourseproject.uzonlineedu.entity.Course;
import uzonlinecourseproject.uzonlineedu.entity.CourseComment;
import uzonlinecourseproject.uzonlineedu.entity.User;
import uzonlinecourseproject.uzonlineedu.exception.ResourceNotFoundException;
import uzonlinecourseproject.uzonlineedu.modify.CourseCommentCreateDto;
import uzonlinecourseproject.uzonlineedu.modify.CourseCommentUpdateDto;
import uzonlinecourseproject.uzonlineedu.repository.CourseCommentRepository;
import uzonlinecourseproject.uzonlineedu.repository.CourseRepository;
import uzonlinecourseproject.uzonlineedu.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class CourseCommentService implements CourseCommentServiceImpl {

    private final CourseCommentRepository courseCommentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Override
    public CourseCommentDto getCourseCommentById(Long id) {
        return courseCommentRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("CourseComment not found"));
    }

    @Override
    public void deleteCourseCommentById(Long id) {
        courseCommentRepository.findById(id).ifPresentOrElse(courseCommentRepository::delete,
                () -> {
                    throw new ResourceNotFoundException("CourseComment not found");
                });
    }

    @Override
    public CourseCommentDto updateCourseCommentById(Long id, CourseCommentUpdateDto courseCommentUpdateDto) {
        return courseCommentRepository.findById(id)
                .map(existingComment -> {
                    if (courseCommentUpdateDto.getContent() != null) {
                        existingComment.setContent(courseCommentUpdateDto.getContent());
                    }
                    return convertToDto(courseCommentRepository.save(existingComment));
                }).orElseThrow(() -> new ResourceNotFoundException("CourseComment not found with id " + id));
    }

    @Override
    public CourseCommentDto createCourseComment(CourseCommentCreateDto courseCommentCreateDto) {

        User user = userRepository.findById(courseCommentCreateDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + courseCommentCreateDto.getUserId()));

        Course course = courseRepository.findById(courseCommentCreateDto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + courseCommentCreateDto.getCourseId()));

        return Optional.of(courseCommentCreateDto)
                .map(req -> {
                    CourseComment comment = new CourseComment();
                    comment.setContent(req.getContent());
                    comment.setCreatedAt(LocalDateTime.now());
                    comment.setUser(user);
                    comment.setCourse(course);
                    return convertToDto(courseCommentRepository.save(comment));
                }).orElseThrow(() -> new ResourceNotFoundException("Invalid course comment data"));
    }

    @Override
    public List<CourseCommentDto> getAllCourseComments() {
        return courseCommentRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Page<CourseCommentDto> getAllCourseComments(Pageable pageable) {
        return courseCommentRepository.findAll(pageable)
                .map(this::convertToDto);
    }

    @Override
    public List<CourseCommentDto> getCourseCommentsByUserId(Long userId) {
        userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User not found with id " + userId));

        return courseCommentRepository.findAll().stream()
                .filter(comment -> comment.getUser() != null && comment.getUser().getId().equals(userId))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Page<CourseCommentDto> getCourseCommentsByUserId(Long userId, Pageable pageable) {
        userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User not found with id " + userId));
        return courseCommentRepository.findByUserId(userId, pageable)
                .map(this::convertToDto);
    }

    @Override
    public List<CourseCommentDto> getCourseCommentsByCourseId(Long courseId) {
        courseRepository.findById(courseId).orElseThrow(() ->
                new ResourceNotFoundException("Course not found with id " + courseId));

        return courseCommentRepository.findAll().stream()
                .filter(comment -> comment.getCourse() != null && comment.getCourse().getId().equals(courseId))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Page<CourseCommentDto> getCourseCommentsByCourseId(Long courseId, Pageable pageable) {
        courseRepository.findById(courseId).orElseThrow(() ->
                new ResourceNotFoundException("Course not found with id " + courseId));
        return courseCommentRepository.findByCourseId(courseId, pageable)
                .map(this::convertToDto);
    }

    @Override
    public List<CourseCommentDto> getRecentCourseComments(int limit) {
        return courseCommentRepository.findAll()
                .stream()
                .sorted((commentOne, commentTwo) -> commentTwo.getCreatedAt().compareTo(commentOne.getCreatedAt()))
                .limit(limit)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(Long id) {
        return courseCommentRepository.existsById(id);
    }

    private CourseCommentDto convertToDto(CourseComment comment) {
        CourseCommentDto commentDto = new CourseCommentDto();
        commentDto.setId(comment.getId());
        commentDto.setContent(comment.getContent());
        commentDto.setCreatedAt(comment.getCreatedAt());
        if (comment.getUser() != null) {
            commentDto.setUserId(comment.getUser().getId());
        }
        if (comment.getCourse() != null) {
            commentDto.setCourseId(comment.getCourse().getId());
        }
        return commentDto;
    }
}
