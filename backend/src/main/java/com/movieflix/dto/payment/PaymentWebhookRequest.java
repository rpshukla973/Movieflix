package com.movieflix.dto.payment;

import com.movieflix.entity.PaymentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PaymentWebhookRequest(
        @NotBlank String transactionId,
        @NotNull PaymentStatus status,
        String payload,
        String signature
) {
}
