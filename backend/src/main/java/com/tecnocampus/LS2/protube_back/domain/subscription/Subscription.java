package com.tecnocampus.LS2.protube_back.domain.subscription;

import com.tecnocampus.LS2.protube_back.domain.user.UserId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Subscription {
    private SubscriptionId id;
    private UserId userId;
    private String channelName;
    private LocalDateTime subscribedAt;

    public Subscription(UserId userId, String channelName) {
        this.id = new SubscriptionId(null);
        this.userId = Objects.requireNonNull(userId);
        this.channelName = Objects.requireNonNull(channelName);
        this.subscribedAt = LocalDateTime.now();
    }
}
