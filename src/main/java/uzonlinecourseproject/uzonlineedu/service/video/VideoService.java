package uzonlinecourseproject.uzonlineedu.service.video;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uzonlinecourseproject.uzonlineedu.dto.VideoDto;
import uzonlinecourseproject.uzonlineedu.entity.Course;
import uzonlinecourseproject.uzonlineedu.entity.Lesson;
import uzonlinecourseproject.uzonlineedu.entity.Video;
import uzonlinecourseproject.uzonlineedu.exception.ResourceNotFoundException;
import uzonlinecourseproject.uzonlineedu.modify.VideoCreateDto;
import uzonlinecourseproject.uzonlineedu.modify.VideoUpdateDto;
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
public class VideoService implements VideoServiceImpl {
    private final VideoRepository videoRepository;
    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;

    @Override
    public VideoDto getVideoById(Long id) {
        return videoRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Video not found"));
    }

    @Override
    public void deleteVideoById(Long id) {
        videoRepository.findById(id).ifPresentOrElse(videoRepository::delete,
                () -> {
                    throw new ResourceNotFoundException("Video not found");
                });
    }

    @Override
    public VideoDto updateVideoById(Long id, VideoUpdateDto videoUpdateDto) {
        return videoRepository.findById(id)
                .map(existingVideo -> {
                    if (videoUpdateDto.getTitle() != null) {
                        existingVideo.setTitle(videoUpdateDto.getTitle());
                    }
                    if (videoUpdateDto.getOriginalFilename() != null) {
                        existingVideo.setOriginalFilename(videoUpdateDto.getOriginalFilename());
                    }
                    if (videoUpdateDto.getDownloadUrl() != null) {
                        existingVideo.setDownloadUrl(videoUpdateDto.getDownloadUrl());
                    }
                    if (videoUpdateDto.getSize() != null) {
                        existingVideo.setSize(videoUpdateDto.getSize());
                    }
                    /*if (videoUpdateDto.getLessonIds() != null) {
                        List<Lesson> lessons = videoUpdateDto.getLessonIds().stream()
                                .map(lessonId -> lessonRepository.findById(lessonId)
                                        .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id " + lessonId)))
                                .collect(Collectors.toList());
                        existingVideo.setLessons(lessons);
                    }
                    if (videoUpdateDto.getCourseIds() != null) {
                        List<Course> courses = videoUpdateDto.getCourseIds().stream()
                                .map(courseId -> courseRepository.findById(courseId)
                                        .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + courseId)))
                                .collect(Collectors.toList());
                        existingVideo.setCourses(courses);
                    }*/
                    return convertToDto(videoRepository.save(existingVideo));
                }).orElseThrow(() -> new ResourceNotFoundException("Video not found with id " + id));
    }

    @Override
    public VideoDto createVideo(VideoCreateDto videoCreateDto) {
        return Optional.of(videoCreateDto)
                .map(req -> {
                    Video video = new Video();
                    video.setTitle(req.getTitle());
                    video.setOriginalFilename(req.getOriginalFilename());
                    video.setDownloadUrl(req.getDownloadUrl());
                    video.setSize(req.getSize());
                    video.setUploadDate(LocalDateTime.now());
                    if (req.getLessonIds() != null) {
                        List<Lesson> lessons = req.getLessonIds().stream()
                                .map(lessonId -> lessonRepository.findById(lessonId)
                                        .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id " + lessonId)))
                                .collect(Collectors.toList());
                        video.setLessons(lessons);
                    }
                   /* if (req.getCourseIds() != null) {
                        List<Course> courses = req.getCourseIds().stream()
                                .map(courseId -> courseRepository.findById(courseId)
                                        .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + courseId)))
                                .collect(Collectors.toList());
                        video.setCourses(courses);
                    }*/
                    return convertToDto(videoRepository.save(video));
                }).orElseThrow(() -> new ResourceNotFoundException("Invalid video data"));
    }

    @Override
    public List<VideoDto> getAllVideos() {
        return videoRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<VideoDto> getRecentVideos(int limit) {
        return videoRepository.findAll()
                .stream()
                .sorted((videoOne, videoTwo) -> videoTwo.getUploadDate().compareTo(videoOne.getUploadDate()))
                .limit(limit)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(Long id) {
        return videoRepository.existsById(id);
    }

    @Override
    public Page<VideoDto> getAllVideos(Pageable pageable) {
        return videoRepository.findAll(pageable)
                .map(this::convertToDto);
    }

    @Override
    public Page<VideoDto> getVideosByCourseId(Long courseId, Pageable pageable) {
        courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + courseId));
        return videoRepository.findByCoursesId(courseId, pageable)
                .map(this::convertToDto);
    }

    @Override
    public Page<VideoDto> getVideosByLessonId(Long lessonId, Pageable pageable) {
        lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id " + lessonId));
        return videoRepository.findByLessonsId(lessonId, pageable)
                .map(this::convertToDto);
    }

    @Override
    public long getAllVideosCount() {
        return videoRepository.count();
    }

    @Override
    public List<VideoDto> getVideosByTitle(String title, Pageable pageable) {
        if (title == null || title.trim().isEmpty()) {
            return getAllVideos();
        }
        return videoRepository.findByTitleContainingIgnoreCase(title, pageable)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private VideoDto convertToDto(Video video) {
        VideoDto videoDto = new VideoDto();
        videoDto.setId(video.getId());
        videoDto.setTitle(video.getTitle());
        videoDto.setOriginalFilename(video.getOriginalFilename());
        videoDto.setDownloadUrl(video.getDownloadUrl());
        videoDto.setSize(video.getSize());
        videoDto.setUploadDate(video.getUploadDate());
        if (video.getLessons() != null) {
            videoDto.setLessonIds(video.getLessons().stream()
                    .map(Lesson::getId)
                    .collect(Collectors.toList()));
        }
        if (video.getCourses() != null) {
            videoDto.setCourseIds(video.getCourses().stream()
                    .map(Course::getId)
                    .collect(Collectors.toList()));
        }
        return videoDto;
    }
}