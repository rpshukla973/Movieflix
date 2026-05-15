package com.movieflix.service;

import com.movieflix.entity.Subscription;
import com.movieflix.entity.SubscriptionPlan;
import com.movieflix.entity.User;

public interface SubscriptionService {
    Subscription createPending(User user, SubscriptionPlan plan);
    Subscription getActiveSubscription(User user);
    boolean hasRequiredPlan(User user, SubscriptionPlan requiredPlan);
    void activate(Long subscriptionId);
}
