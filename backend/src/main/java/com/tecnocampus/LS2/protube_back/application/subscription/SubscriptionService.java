package com.tecnocampus.LS2.protube_back.application.subscription;

import com.tecnocampus.LS2.protube_back.application.dto.mapper.SubscriptionMapper;
import com.tecnocampus.LS2.protube_back.application.dto.subscription.SubscriptionRequest;
import com.tecnocampus.LS2.protube_back.application.dto.subscription.SubscriptionResponse;
import com.tecnocampus.LS2.protube_back.domain.subscription.Subscription;
import com.tecnocampus.LS2.protube_back.domain.subscription.SubscriptionPort;
import com.tecnocampus.LS2.protube_back.domain.user.UserId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SubscriptionService {
    private final SubscriptionPort subscriptionPort;

    public SubscriptionService(SubscriptionPort subscriptionPort) {
        this.subscriptionPort = subscriptionPort;
    }

    @Transactional
    public SubscriptionResponse subscribe(UUID userId, SubscriptionRequest subscriptionRequest) {
        UserId userIdObj = new UserId(userId);

        if (subscriptionPort.existsByUserIdAndChannelName(userIdObj, subscriptionRequest.getChannelName()))
            throw new IllegalStateException("Subscription already exists");

        Subscription subscription = new Subscription(userIdObj, subscriptionRequest.getChannelName());
        Subscription saved = subscriptionPort.save(subscription);

        return SubscriptionMapper.toResponse(saved);
    }

    @Transactional
    public void unsubscribe(UUID userId, String channelName) {
        UserId userIdObj = new UserId(userId);

        Subscription subscription = subscriptionPort.findByUserIdAndChannelName(userIdObj, channelName)
                .orElseThrow(() -> new IllegalStateException("Subscription not found"));

        subscriptionPort.delete(subscription.getId());
    }

    @Transactional(readOnly = true)
    public List<SubscriptionResponse> getUserSubscriptions(UUID userId) {
        UserId userIdObj = new UserId(userId);
        List<Subscription> subscriptions = subscriptionPort.findByUserId(userIdObj);

        return subscriptions.stream()
                .map(SubscriptionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public boolean isSubscribed(UUID userId, String channelName) {
        UserId userIdObj = new UserId(userId);
        return subscriptionPort.existsByUserIdAndChannelName(userIdObj, channelName);
    }
}