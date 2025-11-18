package com.tecnocampus.LS2.protube_back.persistence.jpa.subscription;

import com.tecnocampus.LS2.protube_back.domain.user.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionJpaRepository extends JpaRepository<SubscriptionEntity,Long> {
    List<SubscriptionEntity> findByUserId(Long userId);
    Optional<SubscriptionEntity> findByUserIdAndChannelName(Long userId, String channelName);
    boolean existsByUserIdAndChannelName(Long userId, String channelName);
    long countByUserId(Long userId);
}
