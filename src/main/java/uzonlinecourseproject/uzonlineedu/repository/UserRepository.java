package uzonlinecourseproject.uzonlineedu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uzonlinecourseproject.uzonlineedu.entity.User;
import uzonlinecourseproject.uzonlineedu.enums.GeneralRoles;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByTelegramChatId(Long telegramChatId);

    Optional<User> findByEmail(String email);
    long countByRoles(GeneralRoles role);

}
