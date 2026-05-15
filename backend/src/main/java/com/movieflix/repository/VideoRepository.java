package com.movieflix.repository;

import com.movieflix.entity.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Long> {
    Page<Video> findByActiveTrue(Pageable pageable);
    Page<Video> findByTitleContainingIgnoreCaseAndActiveTrue(String query, Pageable pageable);
}
