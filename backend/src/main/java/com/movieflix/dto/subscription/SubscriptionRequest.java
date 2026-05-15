package com.movieflix.dto.subscription;

import com.movieflix.entity.SubscriptionPlan;
import jakarta.validation.constraints.NotNull;

public record SubscriptionRequest(@NotNull SubscriptionPlan plan) {
}
