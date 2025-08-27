package uzonlinecourseproject.uzonlineedu.modify;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uzonlinecourseproject.uzonlineedu.enums.PayProgress;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentUpdateDto {


    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    private PayProgress payProgress;

    private String transactionId;

    private String telegramPaymentToken;
}