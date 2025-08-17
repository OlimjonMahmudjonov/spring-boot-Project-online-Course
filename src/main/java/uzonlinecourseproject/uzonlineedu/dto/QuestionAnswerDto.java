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
public class QuestionAnswerDto {
    private Long id;
    private String question;
    private String answer;
    private LocalDateTime createdAt;
    private Long userId;
    private Long courseId;
}