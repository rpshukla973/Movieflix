package com.movieflix.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ForgotPasswordResetRequest(
        @Email @NotBlank String email,
        @NotBlank String otp,
        @NotBlank @Size(min = 8, max = 100) String newPassword
) {
}
