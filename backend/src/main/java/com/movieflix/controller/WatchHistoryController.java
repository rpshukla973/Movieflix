package com.movieflix.controller;

import com.movieflix.dto.history.WatchHistoryResponse;
import com.movieflix.dto.history.WatchHistoryUpsertRequest;
import com.movieflix.service.UserContextService;
import com.movieflix.service.WatchHistoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/watch-history")
@RequiredArgsConstructor
public class WatchHistoryController {

    private final WatchHistoryService watchHistoryService;
    private final UserContextService userContextService;

    @PostMapping
    public WatchHistoryResponse upsert(@Valid @RequestBody WatchHistoryUpsertRequest request) {
        return watchHistoryService.upsert(userContextService.getCurrentUser(), request);
    }

    @GetMapping
    public Page<WatchHistoryResponse> list(
            @RequestParam Long profileId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return watchHistoryService.list(userContextService.getCurrentUser(), profileId, page, size);
    }
}