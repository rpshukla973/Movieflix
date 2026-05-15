package com.movieflix.service.impl;

import com.movieflix.dto.profile.ProfileCreateRequest;
import com.movieflix.dto.profile.ProfileResponse;
import com.movieflix.entity.Profile;
import com.movieflix.entity.User;
import com.movieflix.exception.ApiException;
import com.movieflix.repository.ProfileRepository;
import com.movieflix.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;

    @Override
    public ProfileResponse create(User user, ProfileCreateRequest request) {
        Profile profile = new Profile();
        profile.setUser(user);
        profile.setName(request.name().trim());
        profile.setMaturityRating(request.maturityRating());
        return toResponse(profileRepository.save(profile));
    }

    @Override
    public List<ProfileResponse> list(User user) {
        return profileRepository.findByUserIdOrderByIdAsc(user.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public void delete(User user, Long profileId) {
        Profile profile = profileRepository.findByIdAndUserId(profileId, user.getId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Profile not found"));
        profileRepository.delete(profile);
    }

    private ProfileResponse toResponse(Profile profile) {
        return new ProfileResponse(profile.getId(), profile.getName(), profile.getMaturityRating());
    }
}