package uzonlinecourseproject.uzonlineedu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uzonlinecourseproject.uzonlineedu.entity.VerificationToken;

import java.util.Optional;

public interface VerificationTokenRepasting extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
}
