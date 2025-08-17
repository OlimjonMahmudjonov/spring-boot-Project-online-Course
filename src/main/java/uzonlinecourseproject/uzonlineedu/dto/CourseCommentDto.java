package uzonlinecourseproject.uzonlineedu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseCommentDto {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private Long userId;  // User ID
    private Long courseId;  // Course ID
}