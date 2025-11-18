package com.tecnocampus.LS2.protube_back.persistence.jpa.subscription;

import com.tecnocampus.LS2.protube_back.domain.subscription.Subscription;
import com.tecnocampus.LS2.protube_back.domain.subscription.SubscriptionId;
import com.tecnocampus.LS2.protube_back.domain.user.UserId;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionMapper {
    public Subscription toDomain(SubscriptionEntity entity){
        return new Subscription (
                new SubscriptionId(entity.getId()),
                new UserId(entity.getUserId()),
                entity.getChannelName(),
                entity.getSubscribedAt()
        );
    }

    public SubscriptionEntity toEntity(Subscription domain){
        return new SubscriptionEntity(
                domain.getId() != null ? domain.getId().value() : null,
                domain.getUserId().value(),
                domain.getChannelName(),
                domain.getSubscribedAt()
        );
    }
}
