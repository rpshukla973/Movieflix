package com.movieflix.repository;

import com.movieflix.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    List<Profile> findByUserIdOrderByIdAsc(Long userId);
    Optional<Profile> findByIdAndUserId(Long id, Long userId);
}