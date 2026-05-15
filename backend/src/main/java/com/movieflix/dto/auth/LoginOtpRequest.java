package com.movieflix.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginOtpRequest(
        @Email @NotBlank String email,
        @NotBlank String otp
) {
}