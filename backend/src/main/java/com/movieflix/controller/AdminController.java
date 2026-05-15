package com.movieflix.controller;

import com.movieflix.repository.UserRepository;
import com.movieflix.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserRepository userRepository;
    private final VideoRepository videoRepository;

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard() {
        return Map.of(
                "users", userRepository.count(),
                "videos", videoRepository.count()
        );
    }
}
