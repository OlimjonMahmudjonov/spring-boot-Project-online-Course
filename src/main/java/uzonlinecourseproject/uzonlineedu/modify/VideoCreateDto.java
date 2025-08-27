package uzonlinecourseproject.uzonlineedu.modify;

import jakarta.validation.constraints.NotEmpty;
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
public class VideoCreateDto {
    @NotEmpty(message = "Title cannot be empty")
    private String title;

    @NotEmpty(message = "Original filename cannot be empty")
    private String originalFilename;

    @NotEmpty(message = "Download URL cannot be empty")
    private String downloadUrl;

    @NotNull(message = "Size cannot be null")
    @Positive(message = "Size must be positive")
    private Long size;

    private List<Long> lessonIds;

}