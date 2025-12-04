package com.tecnocampus.LS2.protube_back.response;

import com.tecnocampus.LS2.protube_back.application.dto.subscription.SubscriptionResponse;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

    class SubscriptionResponseTest {

        @Test
        void testNoArgsConstructor() {
            SubscriptionResponse subscriptionResponse = new SubscriptionResponse();
            assertNull(subscriptionResponse.getId(), "ID should be null for no-args constructor");
            assertNull(subscriptionResponse.getUserId(), "User ID should be null for no-args constructor");
            assertNull(subscriptionResponse.getChannelName(), "Channel name should be null for no-args constructor");
            assertNull(subscriptionResponse.getSubscribedAt(), "SubscribedAt should be null for no-args constructor");
        }

        @Test
        void testAllArgsConstructor() {
            Long id = 1L;
            UUID userId = UUID.randomUUID();
            String channelName = "TechChannel";
            String subscribedAt = "2023-10-01T10:00:00Z";

            SubscriptionResponse subscriptionResponse = new SubscriptionResponse(id, userId, channelName, subscribedAt);

            assertEquals(id, subscriptionResponse.getId(), "ID should match the constructor argument");
            assertEquals(userId, subscriptionResponse.getUserId(), "User ID should match the constructor argument");
            assertEquals(channelName, subscriptionResponse.getChannelName(), "Channel name should match the constructor argument");
            assertEquals(subscribedAt, subscriptionResponse.getSubscribedAt(), "SubscribedAt should match the constructor argument");
        }
    }