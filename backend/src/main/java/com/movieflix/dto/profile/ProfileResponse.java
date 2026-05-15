package com.movieflix.dto.profile;

public record ProfileResponse(
        Long id,
        String name,
        String maturityRating
) {
}