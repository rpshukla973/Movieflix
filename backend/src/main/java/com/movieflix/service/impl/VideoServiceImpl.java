package com.movieflix.service.impl;

import com.movieflix.entity.Video;
import com.movieflix.exception.ApiException;
import com.movieflix.repository.VideoRepository;
import com.movieflix.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;

    @Override
    public Video upload(String title, String genre, String description, String thumbnailUrl, String videoPath) {
        Video video = new Video();
        video.setTitle(title);
        video.setGenre(genre);
        video.setDescription(description);
        video.setThumbnailUrl(thumbnailUrl);
        video.setVideoPath(videoPath);
        return videoRepository.save(video);
    }

    @Override
    public Page<Video> browse(String query, int page, int size) {
        if (query == null || query.isBlank()) {
            return videoRepository.findByActiveTrue(PageRequest.of(page, size));
        }
        return videoRepository.findByTitleContainingIgnoreCaseAndActiveTrue(query, PageRequest.of(page, size));
    }

    @Override
    public Video getById(Long id) {
        return videoRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Video not found"));
    }
}
