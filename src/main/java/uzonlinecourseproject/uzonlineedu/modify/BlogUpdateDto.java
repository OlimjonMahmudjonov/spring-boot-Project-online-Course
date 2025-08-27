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
public class BlogUpdateDto {

    private String title;
    private String content;
    private Long courseId;
}