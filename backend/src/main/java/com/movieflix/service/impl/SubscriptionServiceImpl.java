package com.movieflix.service.impl;

import com.movieflix.entity.*;
import com.movieflix.exception.ApiException;
import com.movieflix.repository.SubscriptionRepository;
import com.movieflix.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    @Override
    public Subscription createPending(User user, SubscriptionPlan plan) {
        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setPlan(plan);
        subscription.setStatus(SubscriptionStatus.PENDING);
        subscription.setStartDate(LocalDateTime.now());
        subscription.setEndDate(LocalDateTime.now().plusMonths(1));
        return subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription getActiveSubscription(User user) {
        return subscriptionRepository.findFirstByUserAndStatusOrderByEndDateDesc(user, SubscriptionStatus.ACTIVE)
                .filter(sub -> sub.getEndDate().isAfter(LocalDateTime.now()))
                .orElseThrow(() -> new ApiException(HttpStatus.FORBIDDEN, "No active subscription"));
    }

    @Override
    public boolean hasRequiredPlan(User user, SubscriptionPlan requiredPlan) {
        Subscription active = getActiveSubscription(user);
        return active.getPlan().ordinal() >= requiredPlan.ordinal();
    }

    @Override
    @Transactional
    public void activate(Long subscriptionId) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Subscription not found"));
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        subscriptionRepository.save(subscription);
    }
}
