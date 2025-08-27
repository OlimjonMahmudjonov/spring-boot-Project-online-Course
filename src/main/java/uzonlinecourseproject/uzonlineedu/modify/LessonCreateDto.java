package uzonlinecourseproject.uzonlineedu.modify;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LessonCreateDto {
    @NotEmpty(message = "Title cannot be empty")
    private String title;

    private String content;

    private String videoUrl;

    @NotNull(message = "Lesson order cannot be null")
    private Integer lessonOrder;

    @NotNull(message = "Course ID cannot be null")
    private Long courseId;


}