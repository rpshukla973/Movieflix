package com.movieflix.repository;

import com.movieflix.entity.WatchHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WatchHistoryRepository extends JpaRepository<WatchHistory, Long> {
    Optional<WatchHistory> findByUserIdAndProfileIdAndVideoId(Long userId, Long profileId, Long videoId);
    Page<WatchHistory> findByUserIdAndProfileIdOrderByUpdatedAtDesc(Long userId, Long profileId, Pageable pageable);
}