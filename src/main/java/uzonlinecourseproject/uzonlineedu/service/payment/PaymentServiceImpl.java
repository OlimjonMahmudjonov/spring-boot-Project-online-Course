package uzonlinecourseproject.uzonlineedu.service.payment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uzonlinecourseproject.uzonlineedu.dto.PaymentDto;
import uzonlinecourseproject.uzonlineedu.modify.PaymentCreateDto;
import uzonlinecourseproject.uzonlineedu.modify.PaymentUpdateDto;


import java.util.List;

public interface PaymentServiceImpl {


    PaymentDto getPaymentById(Long id);

    void deletePaymentById(Long id);

    PaymentDto updatePaymentById(Long id, PaymentUpdateDto paymentUpdateDto);

    PaymentDto createPayment(PaymentCreateDto paymentCreateDto);

    List<PaymentDto> getAllPayments();

    List<PaymentDto> getPaymentsByUserId(Long userId);

    List<PaymentDto> getPaymentsByCourseId(Long courseId);

    List<PaymentDto> getRecentPayments(int limit);

    boolean existsById(Long id);

    Page<PaymentDto> getAllPayments(Pageable pageable);

    Page<PaymentDto> getPaymentsByUserId(Long userId, Pageable pageable);

    Page<PaymentDto> getPaymentsByCourseId(Long courseId, Pageable pageable);
}
