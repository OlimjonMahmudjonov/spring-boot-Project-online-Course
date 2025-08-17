package uzonlinecourseproject.uzonlineedu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uzonlinecourseproject.uzonlineedu.entity.Payment;
import uzonlinecourseproject.uzonlineedu.entity.User;
import uzonlinecourseproject.uzonlineedu.enums.PayProgress;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Page<Payment> findByUserId(Long userId, Pageable pageable);

    Page<Payment> findByCourseId(Long courseId, Pageable pageable);

  //  List<Payment> findUserAndPayProgress(User user, PayProgress payProgress);

    List<Payment> findByUser(User user);
    List<Payment> findByUserAndPayProgress(User user, PayProgress payProgress);
}
