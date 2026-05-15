package com.movieflix.dto.history;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record WatchHistoryUpsertRequest(
        @NotNull Long profileId,
        @NotNull Long videoId,
        @NotNull @PositiveOrZero Long lastWatchedPosition
) {
}