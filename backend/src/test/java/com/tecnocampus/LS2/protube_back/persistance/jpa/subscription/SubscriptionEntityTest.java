package com.tecnocampus.LS2.protube_back.persistance.jpa.subscription;

import com.tecnocampus.LS2.protube_back.persistence.jpa.subscription.SubscriptionEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class SubscriptionEntityTest {

    @Test
    void testAllArgsConstructor() {
        Long id = 1L;
        UUID userId = UUID.randomUUID();
        String channelName = "Test Channel";
        LocalDateTime subscribedAt = LocalDateTime.now();

        SubscriptionEntity subscription = new SubscriptionEntity(id, userId, channelName, subscribedAt);

        assertEquals(id, subscription.getId());
        assertEquals(userId, subscription.getUserId());
        assertEquals(channelName, subscription.getChannelName());
        assertEquals(subscribedAt, subscription.getSubscribedAt());
    }

    @Test
    void testPartialArgsConstructor() {
        UUID userId = UUID.randomUUID();
        String channelName = "Test Channel";
        LocalDateTime subscribedAt = LocalDateTime.now();

        SubscriptionEntity subscription = new SubscriptionEntity(userId, channelName, subscribedAt);

        assertNull(subscription.getId());
        assertEquals(userId, subscription.getUserId());
        assertEquals(channelName, subscription.getChannelName());
        assertEquals(subscribedAt, subscription.getSubscribedAt());
    }

    @Test
    void testNoArgsConstructor() {
        SubscriptionEntity subscription = new SubscriptionEntity();

        assertNull(subscription.getId());
        assertNull(subscription.getUserId());
        assertNull(subscription.getChannelName());
        assertNull(subscription.getSubscribedAt());
    }

    @Test
    void testSettersAndGetters() {
        SubscriptionEntity subscription = new SubscriptionEntity();

        Long id = 1L;
        UUID userId = UUID.randomUUID();
        String channelName = "Updated Channel";
        LocalDateTime subscribedAt = LocalDateTime.now();

        subscription.setId(id);
        subscription.setUserId(userId);
        subscription.setChannelName(channelName);
        subscription.setSubscribedAt(subscribedAt);

        assertEquals(id, subscription.getId());
        assertEquals(userId, subscription.getUserId());
        assertEquals(channelName, subscription.getChannelName());
        assertEquals(subscribedAt, subscription.getSubscribedAt());
    }
}