package com.tecnocampus.LS2.protube_back.persistance.jpa.subscription;

import com.tecnocampus.LS2.protube_back.domain.subscription.Subscription;
import com.tecnocampus.LS2.protube_back.domain.subscription.SubscriptionId;
import com.tecnocampus.LS2.protube_back.domain.user.UserId;
import com.tecnocampus.LS2.protube_back.persistence.jpa.subscription.SubscriptionEntity;
import com.tecnocampus.LS2.protube_back.persistence.jpa.subscription.SubscriptionJpaRepository;
import com.tecnocampus.LS2.protube_back.persistence.jpa.subscription.SubscriptionMapper;
import com.tecnocampus.LS2.protube_back.persistence.jpa.subscription.SubscriptionPersistenceAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubscriptionPersistanceAdapterTest {

    @InjectMocks
    private SubscriptionPersistenceAdapter subscriptionPersistenceAdapter;

    @Mock
    private SubscriptionJpaRepository subscriptionJpaRepository;


    @Test
    void testSave() {
        Subscription subscription = new Subscription(
                new SubscriptionId(1L),
                new UserId(UUID.randomUUID()),
                "Test Channel",
                LocalDateTime.now()
        );

        SubscriptionEntity entity = SubscriptionMapper.toEntity(subscription);
        when(subscriptionJpaRepository.save(any(SubscriptionEntity.class))).thenReturn(entity);

        Subscription savedSubscription = subscriptionPersistenceAdapter.save(subscription);

        assertNotNull(savedSubscription);
        verify(subscriptionJpaRepository, times(1)).save(any(SubscriptionEntity.class));
    }

    @Test
    void testDelete() {
        SubscriptionId subscriptionId = new SubscriptionId(1L);

        subscriptionPersistenceAdapter.delete(subscriptionId);

        verify(subscriptionJpaRepository, times(1)).deleteById(subscriptionId.value());
    }

    @Test
    void testFindByUserId() {
        UUID userId = UUID.randomUUID();
        SubscriptionEntity entity = new SubscriptionEntity(userId, "Test Channel", LocalDateTime.now());
        when(subscriptionJpaRepository.findByUserId(userId)).thenReturn(List.of(entity));

        List<Subscription> subscriptions = subscriptionPersistenceAdapter.findByUserId(new UserId(userId));

        assertEquals(1, subscriptions.size());
        verify(subscriptionJpaRepository, times(1)).findByUserId(userId);
    }

    @Test
    void testFindByUserIdAndChannelName() {
        UUID userId = UUID.randomUUID();
        String channelName = "Test Channel";
        SubscriptionEntity entity = new SubscriptionEntity(userId, channelName, LocalDateTime.now());
        when(subscriptionJpaRepository.findByUserIdAndChannelName(userId, channelName)).thenReturn(Optional.of(entity));

        Optional<Subscription> subscription = subscriptionPersistenceAdapter.findByUserIdAndChannelName(new UserId(userId), channelName);

        assertTrue(subscription.isPresent());
        verify(subscriptionJpaRepository, times(1)).findByUserIdAndChannelName(userId, channelName);
    }

    @Test
    void testExistsByUserIdAndChannelName() {
        UUID userId = UUID.randomUUID();
        String channelName = "Test Channel";
        when(subscriptionJpaRepository.existsByUserIdAndChannelName(userId, channelName)).thenReturn(true);

        boolean exists = subscriptionPersistenceAdapter.existsByUserIdAndChannelName(new UserId(userId), channelName);

        assertTrue(exists);
        verify(subscriptionJpaRepository, times(1)).existsByUserIdAndChannelName(userId, channelName);
    }

    @Test
    void testCountByUserId() {
        UUID userId = UUID.randomUUID();
        when(subscriptionJpaRepository.countByUserId(userId)).thenReturn(5L);

        long count = subscriptionPersistenceAdapter.countByUserId(new UserId(userId));

        assertEquals(5, count);
        verify(subscriptionJpaRepository, times(1)).countByUserId(userId);
    }
}