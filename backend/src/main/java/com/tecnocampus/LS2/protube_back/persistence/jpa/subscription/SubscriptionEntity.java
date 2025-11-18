package com.tecnocampus.LS2.protube_back.persistence.jpa.subscription;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table( name = "subscriptions",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "channel_name"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "channel_name", nullable = false)
    private String channelName;

    @Column(name = "subscribed_at", nullable = false)
    private LocalDateTime subscribedAt;

    public SubscriptionEntity(Long userId, String channelName, LocalDateTime subscribedAt) {
        this.userId = userId;
        this.channelName = channelName;
        this.subscribedAt = subscribedAt;
    }
}
