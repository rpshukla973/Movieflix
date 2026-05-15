package com.movieflix.dto.profile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProfileCreateRequest(
        @NotBlank @Size(max = 80) String name,
        @Size(max = 20) String maturityRating
) {
}