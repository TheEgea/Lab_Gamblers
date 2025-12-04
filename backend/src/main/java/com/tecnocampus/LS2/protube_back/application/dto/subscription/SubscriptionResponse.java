package com.tecnocampus.LS2.protube_back.application.dto.subscription;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;


public class SubscriptionResponse {
    private Long id;
    private UUID userId;
    private String channelName;
    private String subscribedAt;

    public SubscriptionResponse() {
    }

    public SubscriptionResponse(Long id, UUID userId, String channelName, String subscribedAt) {
        this.id = id;
        this.userId = userId;
        this.channelName = channelName;
        this.subscribedAt = subscribedAt;
    }

    public Long getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getSubscribedAt() {
        return subscribedAt;
    }
}
