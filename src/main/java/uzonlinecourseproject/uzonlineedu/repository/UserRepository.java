package uzonlinecourseproject.uzonlineedu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uzonlinecourseproject.uzonlineedu.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByTelegramChatId(Long telegramChatId);

    Optional<User> findByEmail(String email);

    long countByRolesContaining(String instructor);

  //  long countByIsActiveTrueAndRolesContaining(String student);
}
