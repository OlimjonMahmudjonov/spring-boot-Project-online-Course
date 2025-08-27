package uzonlinecourseproject.uzonlineedu.service.user;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uzonlinecourseproject.uzonlineedu.dto.UserDto;
import uzonlinecourseproject.uzonlineedu.email.EmailService;
import uzonlinecourseproject.uzonlineedu.entity.User;
import uzonlinecourseproject.uzonlineedu.entity.VerificationToken;
import uzonlinecourseproject.uzonlineedu.enums.GeneralRoles;
import uzonlinecourseproject.uzonlineedu.exception.AlreadyExistsException;
import uzonlinecourseproject.uzonlineedu.exception.ResourceNotFoundException;
import uzonlinecourseproject.uzonlineedu.modify.UserCreateDto;
import uzonlinecourseproject.uzonlineedu.modify.UserUpdateDto;
import uzonlinecourseproject.uzonlineedu.repository.UserRepository;
import uzonlinecourseproject.uzonlineedu.repository.VerificationTokenRepasting;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService implements UserServiceImpl {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final VerificationTokenRepasting verificationTokenRepasting;

    @Override
    public UserDto getUserById(Long userId) {
        return userRepository.findById(userId)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
    }

    @Override
    public UserDto createUser(UserCreateDto createUser) {
        if (existsUserByUsername(createUser.getUsername())) {
            throw new AlreadyExistsException("Username already exists");
        }
        if (existsUserByEmail(createUser.getEmail())) {
            throw new AlreadyExistsException("Email already exists");
        }

        User user = new User();
        user.setUsername(createUser.getUsername());
        user.setEmail(createUser.getEmail());
        user.setPassword(passwordEncoder.encode(createUser.getPassword()));
        user.setStatus(createUser.getStatus());
        user.setRoles(createUser.getRoles());
        user.setVisible(createUser.getVisible());
        user.setCreatedDate(LocalDateTime.now());
        user.setTelegramChatId(createUser.getTelegramChatId());
        user.setTelegramUserName(createUser.getTelegramUserName());

        User savedUser = userRepository.save(user);
        String verificationToken = UUID.randomUUID().toString();
        VerificationToken verification = new VerificationToken(
                verificationToken,
                savedUser,
                LocalDateTime.now().plusMinutes(3600)
        );

        verificationTokenRepasting.save(verification);


        try {

            emailService.sendVerificationEmail(savedUser.getEmail(), verificationToken);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send verification email");
        }


        return convertToDto(savedUser);
    }

    @Override
    public UserDto update(UserUpdateDto userUpdateDto, Long userId) {
        return userRepository.findById(userId)
                .map(existingUser -> {
                    if (userUpdateDto.getUsername() != null) {
                        if (!userUpdateDto.getUsername().equals(existingUser.getUsername()) && existsUserByUsername(userUpdateDto.getUsername())) {
                            throw new AlreadyExistsException("Username already exists");
                        }
                        existingUser.setUsername(userUpdateDto.getUsername());
                    }
                    if (userUpdateDto.getEmail() != null) {
                        if (!userUpdateDto.getEmail().equals(existingUser.getEmail()) && existsUserByEmail(userUpdateDto.getEmail())) {
                            throw new AlreadyExistsException("Email already exists");
                        }
                        existingUser.setEmail(userUpdateDto.getEmail());
                    }
                    if (userUpdateDto.getPassword() != null) {
                        existingUser.setPassword(passwordEncoder.encode(userUpdateDto.getPassword()));
                    }
                    if (userUpdateDto.getStatus() != null) {
                        existingUser.setStatus(userUpdateDto.getStatus());
                    }
                    if (userUpdateDto.getVisible() != null) {
                        existingUser.setVisible(userUpdateDto.getVisible());
                    }
                    if (userUpdateDto.getTelegramChatId() != null) {
                        existingUser.setTelegramChatId(userUpdateDto.getTelegramChatId());
                    }
                    if (userUpdateDto.getTelegramUserName() != null) {
                        existingUser.setTelegramUserName(userUpdateDto.getTelegramUserName());
                    }
                    return convertToDto(userRepository.save(existingUser));
                }).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).ifPresentOrElse(userRepository::delete,
                () -> {
                    throw new ResourceNotFoundException("User not found with id " + userId);
                });
    }

    @Override
    public Page<UserDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(this::convertToDto);
    }

    @Override
    public void resetEmail(Long userId, String email) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
        if (existsUserByEmail(email) && !email.equals(user.getEmail())) {
            throw new AlreadyExistsException("Email already exists");
        }
        user.setEmail(email);
        userRepository.save(user);
    }

    @Override
    public void resetPassword(Long userId, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

        String resetToken = UUID.randomUUID().toString();
        try {
            emailService.sendPasswordResetEmail(user.getEmail(), resetToken);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send password reset email");
        }


        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public boolean existsUserByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsUserByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public long countStudentActive() {// TODO fix active count
      //  return userRepository.countByIsActiveTrueAndRolesContaining("STUDENT");
        return userRepository.countByRoles(GeneralRoles.ROLE_STUDENT);
    }

    @Override
    public long countInstructor() {// TODO fix

        return userRepository.countByRoles(GeneralRoles.ROLE_INSTRUCTOR);

    }

    private UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setStatus(user.getStatus());
        userDto.setRoles(user.getRoles());
        userDto.setVisible(user.getVisible());
        userDto.setCreatedDate(user.getCreatedDate());
        userDto.setTelegramChatId(user.getTelegramChatId());
        userDto.setTelegramUserName(user.getTelegramUserName());
        return userDto;
    }
}