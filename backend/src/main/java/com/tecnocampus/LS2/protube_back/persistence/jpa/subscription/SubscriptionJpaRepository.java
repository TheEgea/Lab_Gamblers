package com.tecnocampus.LS2.protube_back.persistence.jpa.subscription;

import com.tecnocampus.LS2.protube_back.domain.user.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubscriptionJpaRepository extends JpaRepository<SubscriptionEntity,Long> {
    List<SubscriptionEntity> findByUserId(UUID userId);
    Optional<SubscriptionEntity> findByUserIdAndChannelName(UUID userId, String channelName);
    boolean existsByUserIdAndChannelName(UUID userId, String channelName);
    long countByUserId(UUID userId);
}
