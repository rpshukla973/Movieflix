package com.movieflix.controller;

import com.movieflix.entity.SubscriptionPlan;
import com.movieflix.entity.Video;
import com.movieflix.exception.ApiException;
import com.movieflix.security.SubscriptionGuard;
import com.movieflix.service.StorageService;
import com.movieflix.service.UserContextService;
import com.movieflix.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;
    private final StorageService storageService;
    private final UserContextService userContextService;
    private final SubscriptionGuard subscriptionGuard;

    @GetMapping
    public Page<Video> browse(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return videoService.browse(q, page, size);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Video upload(
            @RequestPart("title") String title,
            @RequestPart("genre") String genre,
            @RequestPart(value = "description", required = false) String description,
            @RequestPart("video") MultipartFile video,
            @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail
    ) {
        String videoPath = storageService.store(video, "videos");
        String thumbnailPath = thumbnail != null ? storageService.store(thumbnail, "thumbnails") : null;
        return videoService.upload(title, genre, description, thumbnailPath, videoPath);
    }

    @GetMapping("/stream/{id}")
    public ResponseEntity<?> stream(@PathVariable Long id, @RequestHeader HttpHeaders headers) {
        subscriptionGuard.assertAccess(userContextService.getCurrentUser(), SubscriptionPlan.BASIC);
        Video video = videoService.getById(id);
        Resource resource = storageService.loadAsResource(video.getVideoPath());

        long contentLength;
        try {
            contentLength = resource.contentLength();
        } catch (IOException ex) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to read video resource");
        }

        MediaType mediaType = MediaTypeFactory.getMediaType(resource)
                .orElse(MediaType.APPLICATION_OCTET_STREAM);

        if (headers.getRange().isEmpty()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                    .contentLength(contentLength)
                    .contentType(mediaType)
                    .body(resource);
        }

        HttpRange range = headers.getRange().get(0);
        ResourceRegion region = range.toResourceRegion(resource);
        long start = region.getPosition();
        long end = Math.min(start + region.getCount() - 1, contentLength - 1);

        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                .header(HttpHeaders.CONTENT_RANGE, "bytes " + start + "-" + end + "/" + contentLength)
                .contentLength(region.getCount())
                .contentType(mediaType)
                .body(region);
    }
}
