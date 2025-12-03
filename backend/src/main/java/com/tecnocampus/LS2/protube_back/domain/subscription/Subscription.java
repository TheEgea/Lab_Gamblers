package com.tecnocampus.LS2.protube_back.domain.subscription;

import com.tecnocampus.LS2.protube_back.domain.user.UserId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Subscription {
    private SubscriptionId id;
    private UserId userId;
    private String channelName;
    private LocalDateTime subscribedAt;

    public Subscription(UserId userId, String channelName) {
        this(new SubscriptionId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE),
                userId,
                channelName,
                LocalDateTime.now());
    }
}