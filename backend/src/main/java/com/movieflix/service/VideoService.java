package com.movieflix.service;

import com.movieflix.entity.Video;
import org.springframework.data.domain.Page;

public interface VideoService {
    Video upload(String title, String genre, String description, String thumbnailUrl, String videoPath);
    Page<Video> browse(String query, int page, int size);
    Video getById(Long id);
}
