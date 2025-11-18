package com.tecnocampus.LS2.protube_back.application.user;

import com.tecnocampus.LS2.protube_back.application.dto.user.UserProfileResponse;
import com.tecnocampus.LS2.protube_back.domain.subscription.Subscription;
import com.tecnocampus.LS2.protube_back.domain.subscription.SubscriptionPort;
import com.tecnocampus.LS2.protube_back.domain.user.User;
import com.tecnocampus.LS2.protube_back.domain.user.UserId;
import com.tecnocampus.LS2.protube_back.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final UserPersistenceAdapter userAdapter;
    private final SubscriptionPort subscriptionPort;

    @Transactional(readOnly = true)
    public UserProfileResponse getUserProfile(Long userId) {
        UserId userIdObj = new UserId(userId);

        User user = userAdapter.findById(userIdObj)
                .orElseThrow(() -> new BusinessException("User not found"));

        List<Subscription> subscriptions = subscriptionPort.findByUserId(userIdObj);
        List<String> channelNames = subscriptions.stream()
                .map(Subscription::getChannelName)
                .collect(Collectors.toList());

        return new UserProfileResponse(
                user.id().value(),
                user.username().value(),
                user.email(),
                subscriptions.size(),
                channelNames
        );
    }
}
