package uzonlinecourseproject.uzonlineedu.service.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uzonlinecourseproject.uzonlineedu.dto.PaymentDto;
import uzonlinecourseproject.uzonlineedu.entity.Course;
import uzonlinecourseproject.uzonlineedu.entity.Payment;
import uzonlinecourseproject.uzonlineedu.entity.User;
import uzonlinecourseproject.uzonlineedu.exception.ResourceNotFoundException;
import uzonlinecourseproject.uzonlineedu.modify.PaymentCreateDto;
import uzonlinecourseproject.uzonlineedu.modify.PaymentUpdateDto;
import uzonlinecourseproject.uzonlineedu.repository.CourseRepository;
import uzonlinecourseproject.uzonlineedu.repository.PaymentRepository;
import uzonlinecourseproject.uzonlineedu.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class PaymentService implements PaymentServiceImpl {
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Override
    public PaymentDto getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
    }

    @Override
    public void deletePaymentById(Long id) {
        paymentRepository.findById(id).ifPresentOrElse(paymentRepository::delete,
                () -> {
                    throw new ResourceNotFoundException("Payment not found");
                });
    }

    @Override
    public PaymentDto updatePaymentById(Long id, PaymentUpdateDto paymentUpdateDto) {
        return paymentRepository.findById(id)
                .map(existingPayment -> {
                    if (paymentUpdateDto.getAmount() != null) {
                        existingPayment.setAmount(paymentUpdateDto.getAmount());
                    }
                    if (paymentUpdateDto.getPayProgress() != null) {
                        existingPayment.setPayProgress(paymentUpdateDto.getPayProgress());
                    }
                    if (paymentUpdateDto.getTransactionId() != null) {
                        existingPayment.setTransactionId(paymentUpdateDto.getTransactionId());
                    }
                    if (paymentUpdateDto.getTelegramPaymentToken() != null) {
                        existingPayment.setTelegramPaymentToken(paymentUpdateDto.getTelegramPaymentToken());
                    }
                    return convertToDto(paymentRepository.save(existingPayment));
                }).orElseThrow(() -> new ResourceNotFoundException("Payment not found with id " + id));
    }

    @Override
    public PaymentDto createPayment(PaymentCreateDto paymentCreateDto) {
        User user = userRepository.findById(paymentCreateDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + paymentCreateDto.getUserId()));
        Course course = courseRepository.findById(paymentCreateDto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + paymentCreateDto.getCourseId()));

        return Optional.of(paymentCreateDto)
                .map(req -> {
                    Payment payment = new Payment();
                    payment.setAmount(req.getAmount());
                    payment.setPaymentDate(LocalDateTime.now());
                    payment.setPayProgress(req.getPayProgress());
                    payment.setTransactionId(req.getTransactionId());
                    payment.setTelegramPaymentToken(req.getTelegramPaymentToken());
                    payment.setUser(user);
                    payment.setCourse(course);
                    return convertToDto(paymentRepository.save(payment));
                }).orElseThrow(() -> new ResourceNotFoundException("Invalid payment data"));
    }

    @Override
    public List<PaymentDto> getAllPayments() {
        return paymentRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentDto> getPaymentsByUserId(Long userId) {
        userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User not found with id " + userId));

        return paymentRepository.findAll().stream()
                .filter(payment -> payment.getUser() != null && payment.getUser().getId().equals(userId))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentDto> getPaymentsByCourseId(Long courseId) {
        courseRepository.findById(courseId).orElseThrow(() ->
                new ResourceNotFoundException("Course not found with id " + courseId));

        return paymentRepository.findAll().stream()
                .filter(payment -> payment.getCourse() != null && payment.getCourse().getId().equals(courseId))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentDto> getRecentPayments(int limit) {
        return paymentRepository.findAll()
                .stream()
                .sorted((paymentOne, paymentTwo) -> paymentTwo.getPaymentDate().compareTo(paymentOne.getPaymentDate()))
                .limit(limit)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(Long id) {
        return paymentRepository.existsById(id);
    }

    @Override
    public Page<PaymentDto> getAllPayments(Pageable pageable) {
        return paymentRepository.findAll(pageable)
                .map(this::convertToDto);
    }

    @Override
    public Page<PaymentDto> getPaymentsByUserId(Long userId, Pageable pageable) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
        return paymentRepository.findByUserId(userId, pageable)
                .map(this::convertToDto);
    }

    @Override
    public Page<PaymentDto> getPaymentsByCourseId(Long courseId, Pageable pageable) {
        courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + courseId));
        return paymentRepository.findByCourseId(courseId, pageable)
                .map(this::convertToDto);
    }

    private PaymentDto convertToDto(Payment payment) {
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setId(payment.getId());
        paymentDto.setAmount(payment.getAmount());
        paymentDto.setPaymentDate(payment.getPaymentDate());
        paymentDto.setPayProgress(payment.getPayProgress());
        paymentDto.setTransactionId(payment.getTransactionId());
        paymentDto.setTelegramPaymentToken(payment.getTelegramPaymentToken());
        if (payment.getUser() != null) {
            paymentDto.setUserId(payment.getUser().getId());
        }
        if (payment.getCourse() != null) {
            paymentDto.setCourseId(payment.getCourse().getId());
        }
        return paymentDto;
    }
}
