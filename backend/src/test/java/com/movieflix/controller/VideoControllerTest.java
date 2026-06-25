package com.movieflix.controller;

import com.movieflix.entity.Video;
import com.movieflix.security.SubscriptionGuard;
import com.movieflix.service.StorageService;
import com.movieflix.service.UserContextService;
import com.movieflix.service.VideoService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VideoControllerTest {

    @Mock
    private VideoService videoService;

    @Mock
    private StorageService storageService;

    @Mock
    private UserContextService userContextService;

    @Mock
    private SubscriptionGuard subscriptionGuard;

    @InjectMocks
    private VideoController videoController;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void streamDoesNotRequireSubscriptionForAnonymousRequests() throws Exception {
        Video video = new Video();
        video.setId(1L);
        video.setVideoPath("videos/demo.mp4");

        Resource resource = new ByteArrayResource("video-bytes".getBytes());

        SecurityContextHolder.getContext().setAuthentication(
                new AnonymousAuthenticationToken("key", "anonymous", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"))
        );

        when(videoService.getById(1L)).thenReturn(video);
        when(storageService.loadAsResource("videos/demo.mp4")).thenReturn(resource);

        ResponseEntity<?> response = videoController.stream(1L, new HttpHeaders());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(subscriptionGuard, never()).assertAccess(any(), any());
    }
}
