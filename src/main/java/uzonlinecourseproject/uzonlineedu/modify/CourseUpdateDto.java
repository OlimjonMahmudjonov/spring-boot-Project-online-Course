package uzonlinecourseproject.uzonlineedu.modify;

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
public class CourseUpdateDto {


    private String title;
    private String description;
    @PositiveOrZero(message = "Original price must be zero or positive")
    private Double originalPrice;
    private Double discountPrice;
    private LocalDateTime discountEndDate;
    private Boolean isFree;
    private String duration;
    private GeneralLevel level;

}