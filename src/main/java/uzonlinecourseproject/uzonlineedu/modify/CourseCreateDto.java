package uzonlinecourseproject.uzonlineedu.modify;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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
public class CourseCreateDto {
    @NotEmpty(message = "Title cannot be empty")
    private String title;

    private String description;

    @PositiveOrZero(message = "Original price must be zero or positive")
    private Double originalPrice;

    private Double discountPrice;

    private LocalDateTime discountEndDate;

    private Boolean isFree;

    private String duration;

    @NotNull(message = "Level cannot be null")
    private GeneralLevel level;

    @NotNull(message = "Category ID cannot be null")
    private Long categoryId;

    private List<Long> lessonIds;
    private Long previewVideoId;
}