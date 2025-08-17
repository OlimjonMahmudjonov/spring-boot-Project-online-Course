package uzonlinecourseproject.uzonlineedu.service.video;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uzonlinecourseproject.uzonlineedu.dto.VideoDto;
import uzonlinecourseproject.uzonlineedu.modify.VideoCreateDto;
import uzonlinecourseproject.uzonlineedu.modify.VideoUpdateDto;


import java.util.List;

public interface VideoServiceImpl {


    VideoDto getVideoById(Long id);

    void deleteVideoById(Long id);

    VideoDto updateVideoById(Long id, VideoUpdateDto videoUpdateDto);

    VideoDto createVideo(VideoCreateDto videoCreateDto);

    List<VideoDto> getAllVideos();

    List<VideoDto> getRecentVideos(int limit);

    boolean existsById(Long id);

    Page<VideoDto> getAllVideos(Pageable pageable);

    Page<VideoDto> getVideosByCourseId(Long courseId, Pageable pageable);

    Page<VideoDto> getVideosByLessonId(Long lessonId, Pageable pageable);

    long  getAllVideosCount();

    List<VideoDto> getVideosByTitle(String title, Pageable pageable);


}
