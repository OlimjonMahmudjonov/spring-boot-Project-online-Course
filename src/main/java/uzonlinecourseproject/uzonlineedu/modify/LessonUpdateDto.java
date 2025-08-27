package uzonlinecourseproject.uzonlineedu.modify;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LessonUpdateDto {

    private String title;
    private String content;
    private String videoUrl;
    private Integer lessonOrder;
    //private Long courseId;
    //private Long videoId;
}