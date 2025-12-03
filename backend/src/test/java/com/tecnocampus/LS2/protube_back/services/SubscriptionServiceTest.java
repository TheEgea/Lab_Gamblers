package com.tecnocampus.LS2.protube_back.services;

import com.tecnocampus.LS2.protube_back.application.dto.mapper.SubscriptionMapper;
import com.tecnocampus.LS2.protube_back.application.dto.subscription.SubscriptionRequest;
import com.tecnocampus.LS2.protube_back.application.dto.subscription.SubscriptionResponse;
import com.tecnocampus.LS2.protube_back.application.subscription.SubscriptionService;
import com.tecnocampus.LS2.protube_back.domain.subscription.Subscription;
import com.tecnocampus.LS2.protube_back.domain.subscription.SubscriptionPort;
import com.tecnocampus.LS2.protube_back.domain.user.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

    @Mock
    private SubscriptionPort subscriptionPort;

    @InjectMocks
    private SubscriptionService subscriptionService;

    @Test
    void testSubscribe() {
        UUID userId = UUID.randomUUID();
        Subscription subscription = new Subscription(new UserId(userId), "channelName");
        SubscriptionRequest request = new SubscriptionRequest("channelName");

        when(subscriptionPort.existsByUserIdAndChannelName(any(), anyString())).thenReturn(false);
        when(subscriptionPort.save(any(Subscription.class))).thenReturn(subscription);

        SubscriptionResponse response = subscriptionService.subscribe(userId, request);

        assertNotNull(response);
        assertEquals("channelName", response.getChannelName());
        verify(subscriptionPort, times(1)).save(any(Subscription.class));
    }

    @Test
    void testSubscribeAlreadyExists() {
        UUID userId = UUID.randomUUID();
        SubscriptionRequest request = new SubscriptionRequest("channelName");

        when(subscriptionPort.existsByUserIdAndChannelName(any(), anyString())).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> subscriptionService.subscribe(userId, request));
        verify(subscriptionPort, never()).save(any(Subscription.class));
    }

    @Test
    void testUnsubscribe() {
        UUID userId = UUID.randomUUID();
        String channelName = "channelName";
        Subscription subscription = new Subscription(new UserId(userId), channelName);

        when(subscriptionPort.findByUserIdAndChannelName(any(), anyString())).thenReturn(Optional.of(subscription));

        subscriptionService.unsubscribe(userId, channelName);

        verify(subscriptionPort, times(1)).delete(subscription.getId());
    }

    @Test
    void testUnsubscribeNotFound() {
        UUID userId = UUID.randomUUID();
        String channelName = "channelName";

        when(subscriptionPort.findByUserIdAndChannelName(any(), anyString())).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> subscriptionService.unsubscribe(userId, channelName));
        verify(subscriptionPort, never()).delete(any());
    }

    @Test
    void testGetUserSubscriptions() {
        UUID userId = UUID.randomUUID();
        Subscription subscription = new Subscription(new UserId(userId), "channelName");

        when(subscriptionPort.findByUserId(any())).thenReturn(List.of(subscription));

        List<SubscriptionResponse> responses = subscriptionService.getUserSubscriptions(userId);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("channelName", responses.get(0).getChannelName());
        verify(subscriptionPort, times(1)).findByUserId(any());
    }

    @Test
    void testIsSubscribed() {
        UUID userId = UUID.randomUUID();
        String channelName = "channelName";

        when(subscriptionPort.existsByUserIdAndChannelName(any(), anyString())).thenReturn(true);

        boolean isSubscribed = subscriptionService.isSubscribed(userId, channelName);

        assertTrue(isSubscribed);
        verify(subscriptionPort, times(1)).existsByUserIdAndChannelName(any(), anyString());
    }
}