package com.tecnocampus.LS2.protube_back.application.dto.subscription;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionResponse {
    private Long id;
    private UUID userId;
    private String channelName;
    private String subscribedAt;
}
