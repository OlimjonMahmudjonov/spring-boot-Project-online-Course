package uzonlinecourseproject.uzonlineedu.service.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uzonlinecourseproject.uzonlineedu.dto.UserDto;
import uzonlinecourseproject.uzonlineedu.modify.UserCreateDto;
import uzonlinecourseproject.uzonlineedu.modify.UserUpdateDto;


import java.util.List;

public interface UserServiceImpl {


    UserDto getUserById(Long userId);

    UserDto createUser(UserCreateDto createUser);

    UserDto update(UserUpdateDto userUpdateDto, Long userId);

    void deleteUser(Long userId);

    Page<UserDto> getAllUsers(Pageable pageable);

    void resetEmail(Long userId, String email);

    void resetPassword(Long userId, String password);

    boolean existsUserByUsername(String username);

    boolean existsUserByEmail(String email);

    long countStudentActive();

    long countInstructor();
}