package uzonlinecourseproject.uzonlineedu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VideoDto {
    private Long id;
    private String title;
    private String originalFilename;
    private String downloadUrl;
    private Long size;
    private LocalDateTime uploadDate;
    private List<Long> lessonIds;  // Darslar ID'lari
    private List<Long> courseIds;  // Kurslar ID'lari (previyu uchun)
}