package com.movieflix.dto.auth;

import com.movieflix.entity.OtpPurpose;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OtpRequest(
        @Email @NotBlank String email,
        @NotNull OtpPurpose purpose
) {
}
