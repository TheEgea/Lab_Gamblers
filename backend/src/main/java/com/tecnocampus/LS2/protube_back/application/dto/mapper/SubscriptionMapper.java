package com.tecnocampus.LS2.protube_back.application.dto.mapper;

import com.tecnocampus.LS2.protube_back.application.dto.subscription.SubscriptionResponse;
import com.tecnocampus.LS2.protube_back.domain.subscription.Subscription;

public class SubscriptionMapper {
    public static SubscriptionResponse toResponse(Subscription subscription) {
        return new SubscriptionResponse(
                subscription.getId().value(),
                subscription.getUserId().value(),
                subscription.getChannelName(),
                subscription.getSubscribedAt().toString()
        );
    }
}
