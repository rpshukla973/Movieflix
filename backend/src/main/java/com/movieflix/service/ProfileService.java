package com.movieflix.service;

import com.movieflix.dto.profile.ProfileCreateRequest;
import com.movieflix.dto.profile.ProfileResponse;
import com.movieflix.entity.User;

import java.util.List;

public interface ProfileService {
    ProfileResponse create(User user, ProfileCreateRequest request);
    List<ProfileResponse> list(User user);
    void delete(User user, Long profileId);
}