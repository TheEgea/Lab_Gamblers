package com.tecnocampus.LS2.protube_back.application.dto.subscription;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SubscriptionRequest {
    private String channelName;

    public SubscriptionRequest() {
    }
    public SubscriptionRequest(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelName() {
        return channelName;
    }
}
