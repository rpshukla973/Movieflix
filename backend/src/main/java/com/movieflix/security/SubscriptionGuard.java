package com.movieflix.security;

import com.movieflix.entity.SubscriptionPlan;
import com.movieflix.entity.User;
import com.movieflix.exception.ApiException;
import com.movieflix.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriptionGuard {

    private final SubscriptionService subscriptionService;

    public void assertAccess(User user, SubscriptionPlan requiredPlan) {
        if (!subscriptionService.hasRequiredPlan(user, requiredPlan)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Required subscription plan not available");
        }
    }
}
