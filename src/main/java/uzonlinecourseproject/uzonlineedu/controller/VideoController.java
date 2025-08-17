package uzonlinecourseproject.uzonlineedu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uzonlinecourseproject.uzonlineedu.resAndreq.ApiResponse;
import uzonlinecourseproject.uzonlineedu.dto.VideoDto;
import uzonlinecourseproject.uzonlineedu.exception.ResourceNotFoundException;
import uzonlinecourseproject.uzonlineedu.modify.VideoCreateDto;
import uzonlinecourseproject.uzonlineedu.modify.VideoUpdateDto;
import uzonlinecourseproject.uzonlineedu.service.video.VideoService;

import java.util.List;

@RestController
@RequestMapping("/api/video")
@RequiredArgsConstructor
public class VideoController {
    private final VideoService videoService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createVideo(@RequestBody VideoCreateDto videoCreateDto) {
        try {
            VideoDto videoDto = videoService.createVideo(videoCreateDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("Video created successfully", videoDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateVideo(@PathVariable Long id, @RequestBody VideoUpdateDto videoUpdateDto) {
        try {
            VideoDto videoDto = videoService.updateVideoById(id, videoUpdateDto);
            return ResponseEntity.ok(new ApiResponse("Video updated successfully", videoDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteVideo(@PathVariable Long id) {
        try {
            videoService.deleteVideoById(id);
            return ResponseEntity.ok(new ApiResponse("Video deleted successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getVideoById(@PathVariable Long id) {
        try {
            VideoDto videoDto = videoService.getVideoById(id);
            return ResponseEntity.ok(new ApiResponse("Video retrieved successfully", videoDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse> getAllVideos(Pageable pageable) {
        Page<VideoDto> videoPage = videoService.getAllVideos(pageable);
        return ResponseEntity.ok(new ApiResponse("Videos retrieved successfully", videoPage.getContent()));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse> getVideosByCourseId(@PathVariable Long courseId, Pageable pageable) {
        try {
            Page<VideoDto> videoPage = videoService.getVideosByCourseId(courseId, pageable);
            return ResponseEntity.ok(new ApiResponse("Videos retrieved successfully", videoPage.getContent()));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<ApiResponse> getVideosByLessonId(@PathVariable Long lessonId, Pageable pageable) {
        try {
            Page<VideoDto> videoPage = videoService.getVideosByLessonId(lessonId, pageable);
            return ResponseEntity.ok(new ApiResponse("Videos retrieved successfully", videoPage.getContent()));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/recent")
    public ResponseEntity<ApiResponse> getRecentVideos(@RequestParam(defaultValue = "10") int limit) {
        List<VideoDto> videoDtos = videoService.getRecentVideos(limit);
        return ResponseEntity.ok(new ApiResponse("Recent videos retrieved successfully", videoDtos));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> getVideosByTitle(@RequestParam String title, Pageable pageable) {
        List<VideoDto> videoDtos = videoService.getVideosByTitle(title, pageable);
        return ResponseEntity.ok(new ApiResponse("Videos retrieved successfully", videoDtos));
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<ApiResponse> existsById(@PathVariable Long id) {
        boolean exists = videoService.existsById(id);
        return ResponseEntity.ok(new ApiResponse("Video existence checked", exists));
    }

    @GetMapping("/count")
    public ResponseEntity<ApiResponse> getAllVideosCount() {
        long count = videoService.getAllVideosCount();
        return ResponseEntity.ok(new ApiResponse("Total videos count retrieved successfully", count));
    }
}