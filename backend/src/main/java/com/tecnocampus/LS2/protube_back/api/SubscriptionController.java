package com.tecnocampus.LS2.protube_back.api;

import com.tecnocampus.LS2.protube_back.application.dto.subscription.SubscriptionRequest;
import com.tecnocampus.LS2.protube_back.application.dto.subscription.SubscriptionResponse;
import com.tecnocampus.LS2.protube_back.application.subscription.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<SubscriptionResponse> subscribe(
            Authentication authentication,
            @RequestBody SubscriptionRequest request) {
        UUID userId = UUID.fromString(authentication.getName());
        SubscriptionResponse response = subscriptionService.subscribe(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{channelName}")
    public ResponseEntity<Void> unsubscribe(
            Authentication authentication,
            @PathVariable String channelName) {
        UUID userId = UUID.fromString(authentication.getName());
        subscriptionService.unsubscribe(userId, channelName);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionResponse>> getUserSubscriptions(Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        List<SubscriptionResponse> subscriptions = subscriptionService.getUserSubscriptions(userId);
        return ResponseEntity.ok(subscriptions);
    }

    @GetMapping("/check/{channelName}")
    public ResponseEntity<Boolean> isSubscribed(
            Authentication authentication,
            @PathVariable String channelName) {
        UUID userId = UUID.fromString(authentication.getName());
        boolean isSubscribed = subscriptionService.isSubscribed(userId, channelName);
        return ResponseEntity.ok(isSubscribed);
    }
}
