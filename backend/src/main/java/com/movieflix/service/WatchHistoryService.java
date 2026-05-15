package com.movieflix.service;

import com.movieflix.dto.history.WatchHistoryResponse;
import com.movieflix.dto.history.WatchHistoryUpsertRequest;
import com.movieflix.entity.User;
import org.springframework.data.domain.Page;

public interface WatchHistoryService {
    WatchHistoryResponse upsert(User user, WatchHistoryUpsertRequest request);
    Page<WatchHistoryResponse> list(User user, Long profileId, int page, int size);
}