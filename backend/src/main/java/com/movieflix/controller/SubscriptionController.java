package com.movieflix.controller;

import com.movieflix.dto.subscription.SubscriptionRequest;
import com.movieflix.entity.Subscription;
import com.movieflix.service.SubscriptionService;
import com.movieflix.service.UserContextService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final UserContextService userContextService;

    @PostMapping
    public Subscription create(@Valid @RequestBody SubscriptionRequest request) {
        return subscriptionService.createPending(userContextService.getCurrentUser(), request.plan());
    }

    @GetMapping("/active")
    public Subscription active() {
        return subscriptionService.getActiveSubscription(userContextService.getCurrentUser());
    }
}
