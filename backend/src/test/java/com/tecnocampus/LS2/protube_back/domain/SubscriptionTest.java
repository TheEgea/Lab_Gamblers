package com.tecnocampus.LS2.protube_back.domain;

import com.tecnocampus.LS2.protube_back.domain.subscription.Subscription;
import com.tecnocampus.LS2.protube_back.domain.subscription.SubscriptionId;
import com.tecnocampus.LS2.protube_back.domain.user.UserId;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class SubscriptionTest {

    @Test
    void testSubscriptionAllArguments() {
        SubscriptionId subscriptionId = new SubscriptionId(1L);
        UserId userId = new UserId(UUID.randomUUID());
        Subscription subscription = new Subscription(
                subscriptionId,
                userId,
                "channelName",
                LocalDateTime.now());

        assertEquals(subscriptionId, subscription.getId());
        assertEquals(userId, subscription.getUserId());
        assertEquals("channelName", subscription.getChannelName());
    }

    @Test
    void testSubscriptionPartialArguments() {
        UserId userId = new UserId(UUID.randomUUID());
        Subscription subscription = new Subscription(
                userId,
                "channelName");

        assertNotNull(subscription.getId());
        assertEquals(userId, subscription.getUserId());
        assertEquals("channelName", subscription.getChannelName());
    }

    @Test
    void testSubscriptionNoArguments() {
        Subscription subscription = new Subscription();

        assertNull(subscription.getId());
        assertNull(subscription.getUserId());
        assertNull(subscription.getChannelName());
        assertNull(subscription.getSubscribedAt());
    }

}
