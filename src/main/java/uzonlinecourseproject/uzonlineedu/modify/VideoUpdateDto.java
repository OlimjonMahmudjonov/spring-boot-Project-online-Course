package uzonlinecourseproject.uzonlineedu.modify;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VideoUpdateDto {
    @NotNull(message = "ID cannot be null")
    private Long id;

    private String title;
    private String originalFilename;
    private String downloadUrl;
    @Positive(message = "Size must be positive")
    private Long size;
    private List<Long> lessonIds;  // Ixtiyoriy
    private List<Long> courseIds;  // Ixtiyoriy
}