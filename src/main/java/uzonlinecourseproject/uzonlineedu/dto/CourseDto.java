package uzonlinecourseproject.uzonlineedu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uzonlinecourseproject.uzonlineedu.enums.GeneralLevel;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {
    private Long id;
    private String title;
    private String description;
    private Double originalPrice;
    private Double discountPrice;
    private LocalDateTime discountEndDate;
    private Boolean isFree;
    private String duration;
    private GeneralLevel level;
    private Long categoryId;  // Category ID
    private List<LessonDto> lessons;
    private List<ReviewDto> reviews;
    private List<CourseCommentDto> comments;
    private List<Long> enrolledUserIds;
    private Long previewVideoId;
}