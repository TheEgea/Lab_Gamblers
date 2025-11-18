package com.tecnocampus.LS2.protube_back.domain.subscription;

import com.tecnocampus.LS2.protube_back.domain.user.UserId;

import java.util.List;
import java.util.Optional;

public interface SubscriptionPort {
    Subscription save(Subscription subscription);
    void delete(SubscriptionId subscriptionId);
    List<Subscription> findByUserId(UserId userId);
    Optional<Subscription> findByUserIdAndChannelName(UserId userId, String channelName);
    boolean existsByUserIdAndChannelName(UserId userId, String channelName);
    long countByUserId(UserId userId);
}
