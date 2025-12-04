package com.tecnocampus.LS2.protube_back.request;

import com.tecnocampus.LS2.protube_back.application.dto.subscription.SubscriptionRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

    class SubscriptionRequestTest {

        @Test
        void testNoArgsConstructor() {
            SubscriptionRequest subscriptionRequest = new SubscriptionRequest();
            assertNull(subscriptionRequest.getChannelName(), "Channel name should be null for no-args constructor");
        }

        @Test
        void testAllArgsConstructor() {
            String channelName = "TechChannel";
            SubscriptionRequest subscriptionRequest = new SubscriptionRequest(channelName);
            assertEquals(channelName, subscriptionRequest.getChannelName(), "Channel name should match the constructor argument");
        }

        @Test
        void testSetChannelName() {
            SubscriptionRequest subscriptionRequest = new SubscriptionRequest();
            String channelName = "MusicChannel";
            subscriptionRequest = new SubscriptionRequest(channelName);
            assertEquals(channelName, subscriptionRequest.getChannelName(), "Channel name should be set correctly");
        }
    }