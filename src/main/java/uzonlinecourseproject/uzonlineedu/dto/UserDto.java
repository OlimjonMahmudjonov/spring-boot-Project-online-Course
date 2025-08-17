package uzonlinecourseproject.uzonlineedu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uzonlinecourseproject.uzonlineedu.enums.GeneralRoles;
import uzonlinecourseproject.uzonlineedu.enums.GeneralsStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String username;
    private String email;
    private GeneralsStatus status;
    private GeneralRoles roles;
    private Boolean visible;
    private LocalDateTime createdDate;
    private Long telegramChatId;
    private String telegramUserName;
    private List<Long> enrolledCourseIds;
    private List<Long> reviewIds;
    private List<Long> commentIds;
    private List<Long> paymentIds;
    private List<Long> blogIds;
    private List<Long> questionAnswerIds;
}