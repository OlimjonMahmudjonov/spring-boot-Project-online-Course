package uzonlinecourseproject.uzonlineedu.modify;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uzonlinecourseproject.uzonlineedu.enums.GeneralRoles;
import uzonlinecourseproject.uzonlineedu.enums.GeneralsStatus;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto {

    private String username;
    private String email;
    private String password;
    private GeneralsStatus status;
    private GeneralRoles roles;
    private Boolean visible;
    private Long telegramChatId;
    private String telegramUserName;
   // private List<Long> enrolledCourseIds;
}