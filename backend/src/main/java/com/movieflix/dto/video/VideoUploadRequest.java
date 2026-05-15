package com.movieflix.dto.video;

import jakarta.validation.constraints.NotBlank;

public record VideoUploadRequest(
        @NotBlank String title,
        @NotBlank String genre,
        String description
) {
}
