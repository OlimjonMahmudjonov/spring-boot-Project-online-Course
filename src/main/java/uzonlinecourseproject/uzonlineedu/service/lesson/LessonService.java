package uzonlinecourseproject.uzonlineedu.service.lesson;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uzonlinecourseproject.uzonlineedu.dto.LessonDto;
import uzonlinecourseproject.uzonlineedu.entity.Course;
import uzonlinecourseproject.uzonlineedu.entity.Lesson;
import uzonlinecourseproject.uzonlineedu.entity.Video;
import uzonlinecourseproject.uzonlineedu.exception.ResourceNotFoundException;
import uzonlinecourseproject.uzonlineedu.modify.LessonCreateDto;
import uzonlinecourseproject.uzonlineedu.modify.LessonUpdateDto;
import uzonlinecourseproject.uzonlineedu.repository.CourseRepository;
import uzonlinecourseproject.uzonlineedu.repository.LessonRepository;
import uzonlinecourseproject.uzonlineedu.repository.VideoRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class LessonService implements LessonServiceImpl {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final VideoRepository videoRepository;

    @Override
    public LessonDto getLessonById(Long id) {
        return lessonRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));
    }

    @Override
    public void deleteLessonById(Long id) {
        lessonRepository.findById(id).ifPresentOrElse(lessonRepository::delete,
                () -> {
                    throw new ResourceNotFoundException("Lesson not found");
                });
    }

    @Override
    public LessonDto updateLessonById(Long id, LessonUpdateDto lessonUpdateDto) {
        return lessonRepository.findById(id)
                .map(existingLesson -> {
                    if (lessonUpdateDto.getTitle() != null) {
                        existingLesson.setTitle(lessonUpdateDto.getTitle());
                    }
                    if (lessonUpdateDto.getContent() != null) {
                        existingLesson.setContent(lessonUpdateDto.getContent());
                    }
                    if (lessonUpdateDto.getVideoUrl() != null) {
                        existingLesson.setVideoUrl(lessonUpdateDto.getVideoUrl());
                    }
                    if (lessonUpdateDto.getLessonOrder() != null) {
                        existingLesson.setLessonOrder(lessonUpdateDto.getLessonOrder());
                    }
                    if (lessonUpdateDto.getCourseId() != null) {
                        Course course = courseRepository.findById(lessonUpdateDto.getCourseId())
                                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + lessonUpdateDto.getCourseId()));
                        existingLesson.setCourse(course);
                    }
                    if (lessonUpdateDto.getVideoId() != null) {
                        Video video = videoRepository.findById(lessonUpdateDto.getVideoId())
                                .orElseThrow(() -> new ResourceNotFoundException("Video not found with id " + lessonUpdateDto.getVideoId()));
                        existingLesson.setVideo(video);
                    }
                    return convertToDto(lessonRepository.save(existingLesson));
                }).orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id " + id));
    }

    @Override
    public LessonDto createLesson(LessonCreateDto lessonCreateDto) {
        Course course = courseRepository.findById(lessonCreateDto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + lessonCreateDto.getCourseId()));

        return Optional.of(lessonCreateDto)
                .map(req -> {
                    Lesson lesson = new Lesson();
                    lesson.setTitle(req.getTitle());
                    lesson.setContent(req.getContent());
                    lesson.setVideoUrl(req.getVideoUrl());
                    lesson.setLessonOrder(req.getLessonOrder());
                    lesson.setCourse(course);
                    if (req.getVideoId() != null) {
                        Video video = videoRepository.findById(req.getVideoId())
                                .orElseThrow(() -> new ResourceNotFoundException("Video not found with id " + req.getVideoId()));
                        lesson.setVideo(video);
                    }
                    return convertToDto(lessonRepository.save(lesson));
                }).orElseThrow(() -> new ResourceNotFoundException("Invalid lesson data"));
    }

    @Override
    public List<LessonDto> getAllLessons(Pageable pageable) {
        return lessonRepository.findAll(pageable)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<LessonDto> getLessonsByCourseId(Long courseId, Pageable pageable) {
        courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + courseId));

        return lessonRepository.findByCourseId(courseId, pageable)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<LessonDto> getLessonsByTitle(String title, Pageable pageable) {
        if (title == null || title.trim().isEmpty()) {
            return getAllLessons(pageable);
        }
        return lessonRepository.findByTitleContainingIgnoreCase(title, pageable)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(Long id) {
        return lessonRepository.existsById(id);
    }

    @Override
    public long lessonCount() {// TODO fix  count
        return lessonRepository.count();
    }

    private LessonDto convertToDto(Lesson lesson) {
        LessonDto lessonDto = new LessonDto();
        lessonDto.setId(lesson.getId());
        lessonDto.setTitle(lesson.getTitle());
        lessonDto.setContent(lesson.getContent());
        lessonDto.setVideoUrl(lesson.getVideoUrl());
        lessonDto.setLessonOrder(lesson.getLessonOrder());
        if (lesson.getCourse() != null) {
            lessonDto.setCourseId(lesson.getCourse().getId());
        }
        if (lesson.getVideo() != null) {
            lessonDto.setVideoId(lesson.getVideo().getId());
        }
        return lessonDto;
    }
}