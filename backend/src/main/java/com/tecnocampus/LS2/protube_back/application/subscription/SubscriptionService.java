package com.tecnocampus.LS2.protube_back.application.subscription;

import com.tecnocampus.LS2.protube_back.application.dto.subscription.SubscriptionRequest;
import com.tecnocampus.LS2.protube_back.application.dto.subscription.SubscriptionResponse;
import com.tecnocampus.LS2.protube_back.domain.subscription.Subscription;
import com.tecnocampus.LS2.protube_back.domain.subscription.SubscriptionPort;
import com.tecnocampus.LS2.protube_back.domain.user.UserId;
import com.tecnocampus.LS2.protube_back.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionPort subscriptionPort;

    @Transactional
    public SubscriptionResponse subscribe(Long userId, SubscriptionRequest subscriptionRequest) {
        UserId userIdObj = new UserId(userId);

        if (subscriptionPort.existsByUserIdAndChannelName(userIdObj, subscriptionRequest.getChannelName()))
            throw new BusinessException("Subscription already exists");

        Subscription subscription = new Subscription(userIdObj, subscriptionRequest.getChannelName());
        Subscription saved = subscriptionPort.save(subscription);

        return toResponse(saved);
    }

    @Transactional
    public void unsubscribe(Long userId, String channelName) {
        UserId userIdObj = new UserId(userId);

        Subscription subscription = subscriptionPort.findByUserIdAndChannelName(userIdObj, channelName)
                .orElseThrow(() -> new BusinessException("Subscription not found"));

        subscriptionPort.delete(subscription.getId());
    }

    @Transactional(readOnly = true)
    public List<SubscriptionResponse> getUserSubscriptions(Long userId) {
        UserId userIdObj = new UserId(userId);
        List<Subscription> subscriptions = subscriptionPort.findByUserId(userIdObj);

        return subscriptions.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public boolean isSubscribed(Long userId, String channelName) {
        UserId userIdObj = new UserId(userId);
        return subscriptionPort.existsByUserIdAndChannelName(userIdObj, channelName);
    }

    private SubscriptionResponse toResponse(Subscription subscription) {
        return new SubscriptionResponse(
                subscription.getId().value(),
                subscription.getUserId().value(),
                subscription.getChannelName(),
                subscription.getSubscribedAt()
        );
    }
}
