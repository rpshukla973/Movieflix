package com.movieflix.service.impl;

import com.movieflix.dto.history.WatchHistoryResponse;
import com.movieflix.dto.history.WatchHistoryUpsertRequest;
import com.movieflix.entity.Profile;
import com.movieflix.entity.User;
import com.movieflix.entity.Video;
import com.movieflix.entity.WatchHistory;
import com.movieflix.exception.ApiException;
import com.movieflix.repository.ProfileRepository;
import com.movieflix.repository.VideoRepository;
import com.movieflix.repository.WatchHistoryRepository;
import com.movieflix.service.WatchHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WatchHistoryServiceImpl implements WatchHistoryService {

    private final WatchHistoryRepository watchHistoryRepository;
    private final ProfileRepository profileRepository;
    private final VideoRepository videoRepository;

    @Override
    public WatchHistoryResponse upsert(User user, WatchHistoryUpsertRequest request) {
        Profile profile = profileRepository.findByIdAndUserId(request.profileId(), user.getId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Profile not found"));
        Video video = videoRepository.findById(request.videoId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Video not found"));

        WatchHistory history = watchHistoryRepository
                .findByUserIdAndProfileIdAndVideoId(user.getId(), profile.getId(), video.getId())
                .orElseGet(() -> {
                    WatchHistory newEntry = new WatchHistory();
                    newEntry.setUser(user);
                    newEntry.setProfile(profile);
                    newEntry.setVideo(video);
                    return newEntry;
                });

        history.setLastWatchedPosition(request.lastWatchedPosition());
        history.setUpdatedAt(LocalDateTime.now());
        return toResponse(watchHistoryRepository.save(history));
    }

    @Override
    public Page<WatchHistoryResponse> list(User user, Long profileId, int page, int size) {
        profileRepository.findByIdAndUserId(profileId, user.getId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Profile not found"));

        return watchHistoryRepository
                .findByUserIdAndProfileIdOrderByUpdatedAtDesc(user.getId(), profileId, PageRequest.of(page, size))
                .map(this::toResponse);
    }

    private WatchHistoryResponse toResponse(WatchHistory watchHistory) {
        return new WatchHistoryResponse(
                watchHistory.getId(),
                watchHistory.getProfile().getId(),
                watchHistory.getVideo().getId(),
                watchHistory.getVideo().getTitle(),
                watchHistory.getLastWatchedPosition(),
                watchHistory.getUpdatedAt()
        );
    }
}