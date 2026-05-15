package com.movieflix.dto.history;

import java.time.LocalDateTime;

public record WatchHistoryResponse(
        Long id,
        Long profileId,
        Long videoId,
        String videoTitle,
        Long lastWatchedPosition,
        LocalDateTime updatedAt
) {
}