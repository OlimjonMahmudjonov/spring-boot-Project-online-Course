package uzonlinecourseproject.uzonlineedu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uzonlinecourseproject.uzonlineedu.resAndreq.ApiResponse;
import uzonlinecourseproject.uzonlineedu.dto.PaymentDto;
import uzonlinecourseproject.uzonlineedu.exception.ResourceNotFoundException;
import uzonlinecourseproject.uzonlineedu.modify.PaymentCreateDto;
import uzonlinecourseproject.uzonlineedu.modify.PaymentUpdateDto;
import uzonlinecourseproject.uzonlineedu.service.payment.PaymentService;

import java.util.List;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<ApiResponse> createPayment(@RequestBody PaymentCreateDto paymentCreateDto) {
        try {
            PaymentDto paymentDto = paymentService.createPayment(paymentCreateDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("Payment created successfully", paymentDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updatePayment(@PathVariable Long id, @RequestBody PaymentUpdateDto paymentUpdateDto) {
        try {
            PaymentDto paymentDto = paymentService.updatePaymentById(id, paymentUpdateDto);
            return ResponseEntity.ok(new ApiResponse("Payment updated successfully", paymentDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deletePayment(@PathVariable Long id) {
        try {
            paymentService.deletePaymentById(id);
            return ResponseEntity.ok(new ApiResponse("Payment deleted successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getPaymentById(@PathVariable Long id) {
        try {
            PaymentDto paymentDto = paymentService.getPaymentById(id);
            return ResponseEntity.ok(new ApiResponse("Payment retrieved successfully", paymentDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse> getAllPayments(Pageable pageable) {
        Page<PaymentDto> paymentPage = paymentService.getAllPayments(pageable);
        return ResponseEntity.ok(new ApiResponse("Payments retrieved successfully", paymentPage.getContent()));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getPaymentsByUserId(@PathVariable Long userId, Pageable pageable) {
        try {
            Page<PaymentDto> paymentPage = paymentService.getPaymentsByUserId(userId, pageable);
            return ResponseEntity.ok(new ApiResponse("Payments retrieved successfully", paymentPage.getContent()));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse> getPaymentsByCourseId(@PathVariable Long courseId, Pageable pageable) {
        try {
            Page<PaymentDto> paymentPage = paymentService.getPaymentsByCourseId(courseId, pageable);
            return ResponseEntity.ok(new ApiResponse("Payments retrieved successfully", paymentPage.getContent()));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/recent")
    public ResponseEntity<ApiResponse> getRecentPayments(@RequestParam(defaultValue = "10") int limit) {
        List<PaymentDto> paymentDtos = paymentService.getRecentPayments(limit);
        return ResponseEntity.ok(new ApiResponse("Recent payments retrieved successfully", paymentDtos));
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<ApiResponse> existsById(@PathVariable Long id) {
        boolean exists = paymentService.existsById(id);
        return ResponseEntity.ok(new ApiResponse("Payment existence checked", exists));
    }
}