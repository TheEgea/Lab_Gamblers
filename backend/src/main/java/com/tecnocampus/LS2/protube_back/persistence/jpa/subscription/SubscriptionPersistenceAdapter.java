package com.tecnocampus.LS2.protube_back.persistence.jpa.subscription;

import com.tecnocampus.LS2.protube_back.domain.subscription.Subscription;
import com.tecnocampus.LS2.protube_back.domain.subscription.SubscriptionId;
import com.tecnocampus.LS2.protube_back.domain.subscription.SubscriptionPort;
import com.tecnocampus.LS2.protube_back.domain.user.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SubscriptionPersistenceAdapter implements SubscriptionPort {
    private final SubscriptionJpaRepository subscriptionJpaRepository;
    private final SubscriptionMapper subscriptionMapper;

    @Override
    public Subscription save(Subscription subscription) {
        SubscriptionEntity subscriptionEntity = subscriptionMapper.toEntity(subscription);
        SubscriptionEntity savedEntity = subscriptionJpaRepository.save(subscriptionEntity);
        return subscriptionMapper.toDomain(savedEntity);
    }

    @Override
    public void delete(SubscriptionId id) {
        subscriptionJpaRepository.deleteById(id.value());
    }

    @Override
    public List<Subscription> findByUserId(UserId userId) {
        return subscriptionJpaRepository.findByUserId(userId.value())
                .stream()
                .map(subscriptionMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Subscription> findByUserIdAndChannelName(UserId userId, String channelName) {
        return subscriptionJpaRepository.findByUserIdAndChannelName(userId.value(), channelName)
                .map(subscriptionMapper::toDomain);
    }

    @Override
    public boolean existsByUserIdAndChannelName(UserId userId, String channelName) {
        return subscriptionJpaRepository.existsByUserIdAndChannelName(userId.value(), channelName);
    }

    @Override
    public long countByUserId(UserId userId) {
        return subscriptionJpaRepository.countByUserId(userId.value());
    }
}
