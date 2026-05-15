package com.movieflix.dto.payment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PaymentCreateRequest(
        @NotNull Long subscriptionId,
        @NotBlank String paymentMethod
) {
}
