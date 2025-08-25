package uzonlinecourseproject.uzonlineedu.modify;

import jakarta.validation.constraints.NotEmpty;
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
public class UserCreateDto {

    @NotEmpty(message = "Username cannot be empty")
    private String username;

    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    private String password;

    @NotNull(message = "Status cannot be null")
    private GeneralsStatus status;

    @NotNull(message = "Status cannot be null")
    private GeneralRoles roles;

    private Boolean visible;

    private Long telegramChatId;

    private String telegramUserName;

    //private List<Long> enrolledCourseIds;
}