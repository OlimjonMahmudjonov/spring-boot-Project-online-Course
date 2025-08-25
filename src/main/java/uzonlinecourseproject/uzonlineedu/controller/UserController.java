package uzonlinecourseproject.uzonlineedu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uzonlinecourseproject.uzonlineedu.resAndreq.ApiResponse;
import uzonlinecourseproject.uzonlineedu.dto.UserDto;
import uzonlinecourseproject.uzonlineedu.exception.ResourceNotFoundException;
import uzonlinecourseproject.uzonlineedu.modify.UserCreateDto;
import uzonlinecourseproject.uzonlineedu.modify.UserUpdateDto;
import uzonlinecourseproject.uzonlineedu.service.user.UserService;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @GetMapping("/verify-email")
    public ResponseEntity<ApiResponse> verifyEmail(@RequestParam String token) {
        try {

            return ResponseEntity.ok(new ApiResponse("Email  successfully ", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("Failed Token  or token duration end ", null));
        }
    }

    @GetMapping("/reset-password")
    public ResponseEntity<ApiResponse> verifyResetPassword(@RequestParam String token, @RequestParam String newPassword) {
        try {
            return ResponseEntity.ok(new ApiResponse("Parol successfully reset", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("Failed Token  or token duration end   ", null));
        }
    }

    @GetMapping("/count/active_student")
    public ResponseEntity<ApiResponse> countActiveStudent() {
        long count = userService.countStudentActive();
        return ResponseEntity.ok(new ApiResponse("Successfully ", count));
    }

    @GetMapping("/count/instructor")
    public ResponseEntity<ApiResponse> countTeacherInstructor() {
        Long count = userService.countInstructor();
        return ResponseEntity.ok(new ApiResponse("Successfully ", count));
    }


    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createUser(@RequestBody UserCreateDto userCreateDto) {
        try {
            UserDto userDto = userService.createUser(userCreateDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("User created successfully", userDto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable Long userId, @RequestBody UserUpdateDto userUpdateDto) {
        try {
            UserDto userDto = userService.update(userUpdateDto, userId);
            return ResponseEntity.ok(new ApiResponse("User updated successfully", userDto));
        } catch (ResourceNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok(new ApiResponse("User deleted successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/get/user/{userId}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {
        try {
            UserDto userDto = userService.getUserById(userId);
            return ResponseEntity.ok(new ApiResponse("User retrieved successfully", userDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse> getAllUsers(Pageable pageable) {
        Page<UserDto> userPage = userService.getAllUsers(pageable);
        return ResponseEntity.ok(new ApiResponse("Users retrieved successfully", userPage.getContent()));
    }

    @PutMapping("/{userId}/reset-email")
    public ResponseEntity<ApiResponse> resetEmail(@PathVariable Long userId, @RequestParam String email) {
        try {
            userService.resetEmail(userId, email);
            return ResponseEntity.ok(new ApiResponse("Email reset successfully", null));
        } catch (ResourceNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{userId}/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@PathVariable Long userId, @RequestParam String password) {
        try {
            userService.resetPassword(userId, password);
            return ResponseEntity.ok(new ApiResponse("Password reset successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/exists/username")
    public ResponseEntity<ApiResponse> existsByUsername(@RequestParam String username) {
        boolean exists = userService.existsUserByUsername(username);
        return ResponseEntity.ok(new ApiResponse("Username existence checked", exists));
    }

    @GetMapping("/exists/email")
    public ResponseEntity<ApiResponse> existsByEmail(@RequestParam String email) {
        boolean exists = userService.existsUserByEmail(email);
        return ResponseEntity.ok(new ApiResponse("Email existence checked", exists));
    }
}