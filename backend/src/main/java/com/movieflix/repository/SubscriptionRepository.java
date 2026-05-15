package com.movieflix.repository;

import com.movieflix.entity.Subscription;
import com.movieflix.entity.SubscriptionStatus;
import com.movieflix.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findFirstByUserAndStatusOrderByEndDateDesc(User user, SubscriptionStatus status);
}
