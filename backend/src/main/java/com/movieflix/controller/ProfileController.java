package com.movieflix.controller;

import com.movieflix.dto.profile.ProfileCreateRequest;
import com.movieflix.dto.profile.ProfileResponse;
import com.movieflix.service.ProfileService;
import com.movieflix.service.UserContextService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final UserContextService userContextService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProfileResponse create(@Valid @RequestBody ProfileCreateRequest request) {
        return profileService.create(userContextService.getCurrentUser(), request);
    }

    @GetMapping
    public List<ProfileResponse> list() {
        return profileService.list(userContextService.getCurrentUser());
    }

    @DeleteMapping("/{profileId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long profileId) {
        profileService.delete(userContextService.getCurrentUser(), profileId);
    }
}